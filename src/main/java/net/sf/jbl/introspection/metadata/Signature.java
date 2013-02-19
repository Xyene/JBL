package net.sf.jbl.introspection.metadata;

public class Signature {
    public Signature(String signature) {
        int pos;

        if (signature.charAt(0) == '<') {
            char c;
            pos = 2;
            do {
                int end = signature.indexOf(':', pos);
                signature.substring(pos - 1, end);
                pos = end + 1;

                c = signature.charAt(pos);
                if (c == 'L' || c == '[' || c == 'T') {
                    pos = parseType(signature, pos);
                }

                while ((c = signature.charAt(pos++)) == ':') {
                    pos = parseType(signature, pos);
                }
            } while (c != '>');
        } else {
            pos = 0;
        }

        int len = signature.length();
        if (signature.charAt(pos) == '(') {
            pos++;
            while (signature.charAt(pos) != ')') {
                pos = parseType(signature, pos);
            }
            pos = parseType(signature, pos + 1);
            while (pos < len) {
                pos = parseType(signature, pos + 1);
            }
        } else {
            pos = parseType(signature, pos);
            while (pos < len) {
                pos = parseType(signature, pos);
            }
        }
    }

    protected int parseType(String signature, int pos) {
        int start, end;

        switch (signature.charAt(pos++)) {
            case 'Z':
            case 'C':
            case 'B':
            case 'S':
            case 'I':
            case 'F':
            case 'J':
            case 'D':
            case 'V':

                return pos;

            case '[':
                return parseType(signature, pos);

            case 'T':
                end = signature.indexOf(';', pos);

                return end + 1;

            default: // case 'L':
                start = pos;
                while (true) {
                    switch (signature.charAt(pos++)) {
                        case '.':
                        case ';':

                            //TODO: signature.substring(start, pos - 1);
                            return pos;


                        case '<':
                            //TODO: signature.substring(start, pos - 1);

                            top:
                            while (true) {
                                switch (signature.charAt(pos)) {
                                    case '>':
                                        break top;
                                    case '*':
                                        ++pos;

                                        break;
                                    case '+':
                                    case '-':
                                        pos = parseType(signature, pos + 1);

                                        break;
                                    default:
                                        pos = parseType(signature, pos);

                                        break;
                                }
                            }
                    }
                }
        }
    }
}
