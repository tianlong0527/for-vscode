import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CodeGenerator2 {
    public static void main(String[] args) throws IOException {
        // map(class name, class content)
        Map<String, String> classFile = new HashMap<String, String>();
        if (args.length == 0) {
            System.err.println("please input mermaid code file name as argument.");
        } else {
            // get file name
            String fileName = args[0];
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = "";
            boolean Challenge = false;
            String challengeClass = "";
            while ((line = reader.readLine()) != null) {
                // for challenge
                if(line.contains("{")){
                    boolean first = true; // true first means there isn't same class name before
                    Challenge = true;
                    challengeClass = line.trim().split("\\s+")[1];
                    // creat challenge file title
                    for(String key : classFile.keySet()){
                        if(key.equals(challengeClass)){
                            first = false;
                        }
                    }
                    if(first){
                        classFile.put(challengeClass, class_name(challengeClass));
                    }
                    continue;
                }
                if(line.contains("}")){
                    Challenge = false;
                    continue;
                }
                if(Challenge){
                    line = challenge(line,challengeClass);
                }
                // spilt line into words (without space)
                String[] words = line.trim().split("\\s+");
                if (words[0] == "classDiagram" || words.length == 1){
                    continue; // the beginning of mermaid code
                }
                else if (words[0] == "class" || words.length == 2) {
                    // start from class
                    classFile.put(words[1], class_name(words[1]));
                }
                else {
                    // neither class nor classDiagram
                    if (line.contains("(")) {
                        // function of class
                        String content = inFunction(words, line);
                        classFile.put(words[0], classFile.get(words[0]) + content);
                    } else {
                        // variable of class
                        String content = inVariable(words, line);
                        classFile.put(words[0], classFile.get(words[0]) + content);
                    }
                }

            } // while loop end
            reader.close();
            for(String key : classFile.keySet()){
                String filename = key + ".java";
                File file = new File(filename);
                // create files
                if(!file.exists()){
                    file.createNewFile();
                }
                classFile.put(key, classFile.get(key) + "}");
                try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
                    writer.write(classFile.get(key));
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }            
        }
    } // main end

    static String class_name(String className) {
        return "public class " + className + " {\n";
    }

    static String inFunction(String[] words, String line) {
        String content = "";
        if (line.contains("set")) {
            // setter
            isFunction func = new isFunction(words, line);
            String set1 = func.functionName.substring(3, 4).toLowerCase() + func.functionName.substring(4);
            String set2 = func.funcInput.keySet().toArray()[0].toString();
            if(func.P){
                // public
                content += "    public ";
            }
            else{
                // private
                content += "    private ";
            }
            content += func.returnType + " " + func.functionName + "(" + func.funcInput.get(set2) + " " + set2 + ") {\n";
            content += "        this." + set1 + " = " + set2 + ";\n    }\n";
        }
        else if (line.contains("get")) {
            // getter
            isFunction func = new isFunction(words, line);
            String get1 = func.functionName.substring(3, 4).toLowerCase() + func.functionName.substring(4);
            if(func.P){
                // public
                content += "    public ";
            }
            else{
                // private
                content += "    private ";
            }
            content += func.returnType + " " + func.functionName + "() {\n";
            content += "        return " + get1 + ";\n    }\n";
        }
        else {
            // function
            isFunction func = new isFunction(words, line);
            if(func.P){
                // public
                content += "    public ";
            }
            else{
                // private
                content += "    private ";
            }
            content += func.returnType + " " + func.functionName + "(";
            int count = func.funcInput.size() - 1;
            if(func.funcInput.size() != 1){
                for(String key : func.funcInput.keySet()){
                    content += func.funcInput.get(key) + " " + key;
                    if(count != 0){
                        content += ", ";
                        --count;
                    }
                }
            }
            else{
                if(func.funcInput.containsKey("")){
                    content += "";
                }
                else{
                    for(String key : func.funcInput.keySet()){
                        content += func.funcInput.get(key) + " " + key;
                    }
                }
            }
            content += ") {";
            if(func.returnType.equals("void")){
                content += ";}\n";
            }
            else if(func.returnType.equals("String")){
                content += "return \"\";}\n";
            }
            else if(func.returnType.equals("int")){
                content += "return 0;}\n";
            }
            else{
                content += "return false;}\n";
            }
        }
        return content;
    }

    static String inVariable(String[] words, String line) {
        String content = "";
        isVariable var = new isVariable(words,line);
        if(var.P){
            // public
            content += "    public ";
        }
        else{
            // private
            content += "    private ";
        }
        content += var.variableType + " " + var.variableName + ";\n";
        return content;
    }

    static String challenge(String line, String className){
        line = className + " : " + line.trim();
        return line;
    }
}

class isFunction {
    String className;
    String functionName;
    boolean P; // true means public, false means private
    Map<String, String> funcInput = new HashMap<>(); // map<input name,input type>
    String returnType;

    public isFunction(String[] words,String line) {
        StringBuilder sb = new StringBuilder();
        for(String s : words){
            sb.append(s);
        }
        String b = sb.toString();
        this.className = b.substring(0, b.indexOf(":"));
        this.returnType = b.substring(b.indexOf(")") + 1);
        if(b.contains("+")){
            this.P = true;
            int find1 = b.indexOf("+");
            int find2 = b.indexOf("(");
            this.functionName = b.substring(find1 + 1, find2);
        }
        else{
            this.P = false;
            int find1 = b.indexOf("-");
            int find2 = b.indexOf("(");
            this.functionName = b.substring(find1 + 1, find2);
        }
        String input = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
        if(input.equals("")){
            funcInput.put("", "");
        }
        else{
            String[] Input = input.split(",");
            String[] NT = new String[Input.length];
            for(int i = 0;i < Input.length;++i){
                NT = Input[i].trim().split("\\s+");
                funcInput.put(NT[1], NT[0]);
            }
        }
    }
}

class isVariable {
    String className;
    boolean P; // true means public, false means private
    String variableName;
    String variableType;

    public isVariable(String[] words,String line) {
        this.className = words[0];
        if(line.contains("+")){
            // public
            this.P = true;
            if(words[2].equals("+")){ // there is (a) damn space(s) after +
                this.variableType = words[3];
                this.variableName = words[4];
            }
            else{
                words[2] = words[2].substring(1);
                this.variableType = words[2];
                this.variableName = words[3];
            }
        }
        else{
            // pruivate
            this.P = false;
            if(words[2].equals("-")){ // there is (a) damn space(s) after -
                this.variableType = words[3];
                this.variableName = words[4];
            }
            else{
                words[2] = words[2].substring(1);
                this.variableType = words[2];
                this.variableName = words[3];
            }
        }
    }
}