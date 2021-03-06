package org.block.jsf;

import org.block.jsf.data.Visibility;
import org.block.jsf.data.jsfclass.JSFClass;
import org.block.jsf.data.jsfclass.JSFClassType;
import org.block.jsf.data.jsffunction.JSFMethod;
import org.block.jsf.data.jsfvalue.JSFParameter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class JavaStructureDocReader {

    private static final String META_NAME = "name";
    private static final String META_IS_FINAL = "isFinal";
    private static final String META_CLASS_TYPE = "classType";
    private static final String META_VISIBILITY = "visibility";
    private static final String META_EXTENDING = "extending";
    private static final String META_IMPLEMENTING = "implementing";
    private final String htmlDoc;

    public JavaStructureDocReader(String htmlDoc) {
        this.htmlDoc = htmlDoc;
    }

    public JSFClass read() {
        Map<String, Object> classMeta = this.getClassMeta();
        List<JSFMethod> methods = this.getMethods();

        JSFClass.Builder builder = new JSFClass.Builder();
        builder.setClass((String) classMeta.get(META_NAME));
        builder.setType((JSFClassType) classMeta.get(META_CLASS_TYPE));
        builder.setFinal((boolean) classMeta.getOrDefault(META_IS_FINAL, false));
        builder.setVisibility((Visibility) classMeta.get(META_VISIBILITY));
        if (classMeta.containsKey(META_EXTENDING)) {
            builder.setExtending((String) classMeta.get(META_EXTENDING));
        }
        builder.addImplementations((List<String>) classMeta.getOrDefault(META_IMPLEMENTING, new ArrayList<>()));
        builder.addMethods(methods);
        return builder.build();
    }

    public static JavaStructureDocReader valueOf(InputStream stream) throws IOException {
        StringBuilder textBuilder = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        int by;
        while ((by = br.read()) != -1) {
            textBuilder.append((char) by);
        }
        return new JavaStructureDocReader(textBuilder.toString());
    }

    private Map<String, Object> getClassMeta() {
        Map<String, Object> ret = new HashMap<>();
        String target = "";
        for (int A = 0; A < this.htmlDoc.length(); A++) {
            char character = this.htmlDoc.charAt(A);
            target = target + character;
            if (target.endsWith("<pre")) {
                target = target.substring(target.length() - 4);
            }
            if (target.startsWith("<pre>") && target.endsWith("</pre>")) {
                break;
            }
        }
        target = target.substring(5, target.length() - 6);
        String writeup = "";
        String beforeSpan = "";
        String afterSpan = "";
        String extending = "";
        String implementing = "";
        for (int A = 0; A < target.length(); A++) {
            char character = target.charAt(A);

            writeup = writeup + character;
            if (beforeSpan.length() == 0 && writeup.endsWith("<span")) {
                beforeSpan = writeup.substring(0, writeup.length() - 5);
                writeup = writeup.substring(writeup.length() - 5);
            }
            if (afterSpan.length() == 0 && writeup.startsWith("<span") && writeup.endsWith("</span>")) {
                afterSpan = writeup;
                writeup = "";
            }
            if (extending.length() == 0 && writeup.endsWith("extends ")) {
                writeup = "extends ";
            }
            if (extending.length() == 0 && writeup.startsWith("extends ") && writeup.endsWith("</a>")) {
                extending = writeup;
                writeup = "";
            }
            if (writeup.endsWith("implements")) {
                writeup = "implements";
            }
        }
        implementing = writeup;
        if (beforeSpan.contains("class")) {
            ret.put(META_CLASS_TYPE, JSFClassType.CLASS);
        }
        if (beforeSpan.contains("interface")) {
            ret.put(META_CLASS_TYPE, JSFClassType.INTERFACE);
        }
        if (beforeSpan.contains("final")) {
            ret.put(META_IS_FINAL, true);
        }
        if (beforeSpan.contains("public")) {
            ret.put(META_VISIBILITY, Visibility.PUBLIC);
        }
        afterSpan = afterSpan.substring(0, afterSpan.length() - 7);
        for (int A = afterSpan.length() - 1; A >= 0; A--) {
            char character = afterSpan.charAt(A);
            if (character == '>') {
                afterSpan = afterSpan.substring(A + 1);
                break;
            }
        }
        ret.put(META_NAME, afterSpan);

        List<String> extendingClasses = createClassesFromImplements(extending);
        List<String> implementingClasses = createClassesFromImplements(implementing);
        if (!extendingClasses.isEmpty() && !extendingClasses.get(0).equals("java.lang.Object")) {
            ret.put(META_EXTENDING, extendingClasses.get(0));
        }
        ret.put(META_IMPLEMENTING, implementingClasses);
        return ret;
    }

    private List<String> createClassesFromImplements(String implementHTML) {
        List<String> ret = new ArrayList<>();
        String[] args = implementHTML.split(", ");
        for (String link : args) {
            String href = "";
            for (int A = 0; A < link.length(); A++) {
                char character = link.charAt(A);
                href = href + character;
                if (href.endsWith("href=\"")) {
                    href = "";
                }
                if (href.startsWith("../../") && href.endsWith("\" title=")) {
                    break;
                }
            }
            if (!href.startsWith("../../")) {
                continue;
            }
            href = href.substring(0, href.length() - 13).replaceAll(Pattern.quote("../"), "");
            href = href.replaceAll("/", ".");
            ret.add(href);
        }
        return ret;
    }

    private JSFParameter getParameter(String aHref) {
        String link = "";
        String target = "";
        boolean isOpen = false;
        for (int A = 0; A < aHref.length(); A++) {
            char character = aHref.charAt(A);
            if (character == ' ' || character == '\n') {
                target = "";
                continue;
            }
            if (character == '"') {
                isOpen = !isOpen;
                if (!isOpen) {
                    if (target.startsWith("href=\"")) {
                        link = target;
                        continue;
                    }
                }
            }
            target = target + character;
        }

        link = link.substring(15, link.length() - 5).replaceAll("/", ".");

        target = target.substring(6);
        if (target.endsWith(",")) {
            target = target.substring(0, target.length() - 1);
        }
        return new JSFParameter.Builder().setName(target).setType(link).build();
    }

    private JSFMethod getMethod(String html) {
        String returnClass = "";
        String name = "";
        String target = "";
        for (int A = 0; A < html.length(); A++) {
            char character = html.charAt(A);
            target = target + character;
            if (target.endsWith("<code>")) {
                target = "<code>";
            }
            if (target.endsWith("</code>")) {
                if (returnClass.length() == 0) {
                    returnClass = target;
                } else if (name.length() == 0) {
                    name = target;
                }
                target = "";
            }
        }

        String filterName = "";
        for (int A = name.length() - 1; A >= 0; A--) {
            filterName = name.charAt(A) + filterName;
            if (filterName.startsWith("</a>")) {
                filterName = "";
            }
            if (!filterName.endsWith("</code>") && filterName.startsWith("\">")) {
                filterName = filterName.substring(2);
                break;
            }
        }
        String returning = returnClass.substring(6, returnClass.length() - 7);
        if (returning.contains("href")) {
            String ret = "";
            for (int A = 0; A < returning.length(); A++) {
                ret = ret + returning.charAt(A);
                if (ret.endsWith("href=\"")) {
                    ret = "href=\"";
                }
                if (ret.endsWith("\"") && ret.startsWith("href=\"")) {
                    break;
                }
            }
            returning = ret.substring(6, ret.length() - 1);
        }

        JSFMethod method = new JSFMethod.Builder()
                .setReturning(returning)
                .setName(filterName)
                .build();
        return method;
    }

    private List<JSFMethod> getMethods() {
        String methodSum = this.getMethodSummary();
        String builder = "";
        List<JSFMethod> list = new ArrayList<>();
        for (int A = 0; A < methodSum.length(); A++) {
            char character = methodSum.charAt(A);
            builder = builder + character;
            if (builder.endsWith("<tr")) {
                builder = builder.substring(builder.length() - 3);
                continue;
            }
            if (builder.endsWith("</tr>") && builder.startsWith("<tr ")) {
                list.add(getMethod(builder));
                builder = "";
            }
        }
        return list;
    }

    private String getMethodSummary() {
        StringBuilder methodSum = new StringBuilder();
        String target = "";
        boolean isPassed = false;
        for (int A = 0; A < this.htmlDoc.length(); A++) {
            char character = this.htmlDoc.charAt(A);
            if (character == '\n') {
                if (!isPassed && target.endsWith("<h3>Method Summary</h3>")) {
                    isPassed = true;
                }
                if (isPassed) {
                    methodSum.append(character);
                    if (target.endsWith("</table>")) {
                        break;
                    }
                }
                target = "";
                continue;
            }
            target = target + character;
            if (isPassed) {
                methodSum.append(character);
            }
        }
        return methodSum.toString();
    }
}
