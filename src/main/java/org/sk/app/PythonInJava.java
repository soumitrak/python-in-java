package org.sk.app;

import org.python.core.Py;
import org.python.core.PyCode;
import org.python.core.PyDictionary;
import org.python.core.PyList;
import org.python.core.PyNone;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PySyntaxError;
import org.python.core.PyTuple;
import org.python.util.PythonInterpreter;
import org.sk.input.Beach;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

public class PythonInJava {
    private static void log(final String msg) {
        System.err.println("**** " + msg);
    }

    private static PythonInterpreter getPythonInterpreter() {
        // http://bugs.jython.org/issue2355
        final Properties props = new Properties();
        // props.put("python.home","path to the Lib folder");
        // props.put("python.console.encoding", "UTF-8"); // Used to prevent: console: Failed to install '': java.nio.charset.UnsupportedCharsetException: cp0.
        // props.put("python.security.respectJavaAccessibility", "false"); //don't respect java accessibility, so that we can access protected members on subclasses
        // props.put("python.import.site","false");

        final Properties preprops = System.getProperties();

        PythonInterpreter.initialize(preprops, props, new String[0]);

        return new PythonInterpreter();
    }

    private static void printResultRecursive(final Object result) {
        if (result instanceof String) {
            log((String) result);
        } else if (result instanceof PyString) {
            log(((PyString) result).getString());
        } else if (result instanceof PyList) {
            for (Object aPlist : (PyList) result) {
                printResultRecursive(aPlist);
            }
        } else if (result instanceof PyTuple) {
            for (Object aPlist : (PyTuple) result) {
                printResultRecursive(aPlist);
            }
        } else if (result instanceof PyDictionary) {
            for (Object item : ((PyDictionary) result).items()) {
                printResultRecursive(item);
            }
        } else if (result instanceof PyNone) {
            // Ignore None
            log("None");
        } else {
            log("Unknown result type class " + result.getClass().getCanonicalName());
        }
    }

    private static void printResult(final PyObject result) {
        log("Printing result");
        printResultRecursive(result);
    }

    private static void parse(final PyObject parseMethod,
                              final Beach beach,
                              final String name) {
        log("Calling method");
        PyObject result = parseMethod.__call__(
                Py.java2py(beach),
                new PyString(name)
        );

        printResult(result);
    }

    public static void main(String[] args) throws Exception {
        log("Creating interpreter");
        final PythonInterpreter interpreter = getPythonInterpreter();

        try {
            log("Compiling file");
            final PyCode pythonFile = interpreter.compile(new FileReader(new File("test.py")));

            log("Executing file");
            interpreter.exec(pythonFile);

            log("Getting parse method");
            final PyObject parseMethod = interpreter.get("parse");

            final Beach beach = new Beach("Hapuna", "The Big Island");
            parse(parseMethod, beach, "sk");
            log("Msg set in Python : " + beach.getMsg());
        } catch (PySyntaxError error) {
            log("Exception " + error.getClass().getCanonicalName());
            error.printStackTrace();
        }
    }
}
