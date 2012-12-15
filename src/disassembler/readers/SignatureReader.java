package disassembler.readers;

import com.github.Icyene.bytecode.introspection.internal.members.Member;
import com.github.Icyene.bytecode.introspection.internal.metadata.BaseType;

import java.util.LinkedList;

public class SignatureReader {

    private String returnType;
    private LinkedList<String> parameterTypes = new LinkedList<String>();

    public SignatureReader(Member mem) {
        String descriptor = mem.getDescriptor().getStringValue();
        int separatorPos = descriptor.lastIndexOf(")");
        this.returnType = tokenize(descriptor.substring(separatorPos + 1, descriptor.length())).get(0);
        this.parameterTypes = tokenize(descriptor.substring(1, separatorPos));
    }

    public LinkedList<String> tokenize(String inString) {
        LinkedList<String> params = new LinkedList<String>();
        String remainingParams = inString;
        boolean isArray = false;
        while (remainingParams.length() != 0) {
            char c = remainingParams.substring(0, 1).toCharArray()[0];
            switch (c) {
                case '[':
                    isArray = true;
                    remainingParams = remainingParams.substring(1, remainingParams.length());
                    break;
                case 'L':
                    int semicolonPosition = remainingParams.indexOf(";");
                    params.add(remainingParams.substring(1, semicolonPosition).replace("/", ".") + (isArray ? "[]" : ""));
                    remainingParams = remainingParams.substring(semicolonPosition + 1, remainingParams.length());
                    isArray = false;
                    break;
                case 'I':
                case 'C':
                case 'D':
                case 'F':
                case 'J':
                case 'S':
                case 'Z':
                case 'V':
                case 'B':
                    params.add(BaseType.valueOf(c + "").getReal() + (isArray ? "[]" : ""));
                    remainingParams = remainingParams.substring(1, remainingParams.length());
                    isArray = false;
                    break;
            }
        }
        return params;
    }

    public String getType() {
        return returnType;
    }

    public LinkedList<String> getAugmentingTypes() {
        return parameterTypes;
    }
}
