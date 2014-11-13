package ro.z2h;

import org.codehaus.jackson.map.ObjectMapper;
import ro.z2h.annotation.MyController;
import ro.z2h.annotation.MyRequestMethod;
import ro.z2h.controller.DepartmentController;
import ro.z2h.controller.EmployeeController;
import ro.z2h.fmk.AnnotationScanUtils;
import ro.z2h.fmk.MethodAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dumitru on 11.11.2014.
 */
public class MyDispatcherServlet extends HttpServlet {

    HashMap<String, MethodAttributes> hmPath = new HashMap<String, MethodAttributes>();

    /**
     * la primul request se instantieaza!!
     * se apeleaza o singura data, la nivel de server
     * o singura instanta a unui servlet
     */
    @Override
    public void init() throws ServletException {
        /* Initialize controller pool. */
        try {
            Iterable<Class> classes = AnnotationScanUtils.getClasses("ro.z2h.controller");

            for (Class aClass : classes) {
                //din pachetul de reflection!!
                if(aClass.isAnnotationPresent(MyController.class)) {
                    MyController mc = (MyController)aClass.getAnnotation(MyController.class);
                    //System.out.println(mc.urlPath());

                    Method[] methods = aClass.getMethods();
                    //iter + tab -> pentru prescurtare
                    for(Method met : methods) {
                        if(met.isAnnotationPresent(MyRequestMethod.class)) {
                            MyRequestMethod mrm = (MyRequestMethod)met.getAnnotation(MyRequestMethod.class);
                            //System.out.println(mrm.urlPath() + " " + mrm.methodType());
                            System.out.println(mc.urlPath() + mrm.urlPath() + " " + mrm.methodType());
                            //cheie
                            String path = mc.urlPath() + mrm.urlPath();
                            //value Clasa, Metoda, Tip metoda
                            MethodAttributes ma = new MethodAttributes();
                            ma.setMethodName(met.getName());
                            ma.setControllerClass(aClass.getName());
                            ma.setMethodType(mrm.methodType());
                            //TODO
                            //add param types
                            ma.setMethodParameterTypes(met.getParameterTypes());
                            hmPath.put(path, ma);
                        }
                    }
                }
            }

            System.out.println(hmPath);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatchReply("GET", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatchReply("POST", req, resp);
    }

    private void dispatchReply(String httpMethod, HttpServletRequest req, HttpServletResponse resp) {
        //TODO
        /*Dispatch*/
        Object objDispatch = null;
        try {
            objDispatch = dispatch(httpMethod, req, resp);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        /*Reply*/
        try {
            reply(objDispatch, req, resp);
        } catch (IOException e) {
            e.printStackTrace();
            sendException(e, req, resp);
        }
    }

    /* Where an application controller should be called. */
    private Object dispatch(String httpMethod, HttpServletRequest req, HttpServletResponse resp) throws NoSuchMethodException, InvocationTargetException {
        //TODO
        /* pentru /test = Hello! */
        /* pentru /employee = allEmployees de la app controller */

        String pathInfo = req.getPathInfo();
        /*if(pathInfo.startsWith("/employees")) {
            EmployeeController ec = new EmployeeController();
            return ec.getAllEmployees();
        } else if(pathInfo.startsWith("/department")) {
            DepartmentController dc = new DepartmentController();
            return dc.getAllDepartments();
        }
        return "Hello from Z2H!";*/
        //Map<String, String[]> parameterMap = req.getParameterMap();
        MethodAttributes metAtt = hmPath.get(pathInfo);
        if(metAtt != null) {
            //am un app controllet care sa-mi proceseze
            try {
                Class<?> controllerClass = Class.forName(metAtt.getControllerClass());
                String methodName = metAtt.getMethodName();
                Object controllerInstance = controllerClass.newInstance();
                Method method = controllerClass.getMethod(methodName, metAtt.getMethodParameterTypes());

                Parameter[] parameters = method.getParameters();
                List<String> methodParamValues = new ArrayList<String>();
                for(Parameter param : parameters) {

                    String value = req.getParameter(param.getName());
                    methodParamValues.add(value);
                }

                return method.invoke(controllerInstance, (String[])methodParamValues.toArray(new String[0]));

                /*String methodName = metAtt.getMethodName();
                Class[] parameterTypes = new Class[] {Long.class};
                Object[] parameters = null;
                Object invoke = null;

                if(req.getParameter("id") != null) {
                    parameters = new Object[]{new Long(req.getParameter("id"))};

                    Method method = controllerClass.getMethod(methodName, parameterTypes);
                    Object controllerInstance = controllerClass.newInstance();
                    invoke = method.invoke(controllerInstance, parameters);
                } else {

                    Method method = controllerClass.getMethod(methodName);
                    Object controllerInstance = controllerClass.newInstance();
                    invoke = method.invoke(controllerInstance);
                }
                return invoke;*/
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return "Hello from Z2H";
    }

    /* Used to send the view to the client. */
    private void reply(Object objDispatch, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //TODO
        PrintWriter writer = resp.getWriter();
        //convert to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String str = objectMapper.writeValueAsString(objDispatch);
        writer.write(str);
    }

    /* Used to send an exception to the client. */
    private void sendException(Exception ex, HttpServletRequest req, HttpServletResponse resp) {
        //TODO
        System.out.println("There was an exception!");
    }
}