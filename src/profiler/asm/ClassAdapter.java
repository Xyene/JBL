// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package profiler.asm;


// Referenced classes of package profiler.asm:
//            ClassVisitor, MethodAdapter, AnnotationVisitor, Attribute, 
//            FieldVisitor, MethodVisitor

public class ClassAdapter
    implements ClassVisitor
{

    public ClassAdapter(ClassVisitor classvisitor)
    {
        cv = classvisitor;
    }

    public void visit(int i, int j, String s, String s1, String s2, String as[])
    {
        cv.visit(i, j, s, s1, s2, as);
    }

    public void visitSource(String s, String s1)
    {
        cv.visitSource(s, s1);
    }

    public void visitOuterClass(String s, String s1, String s2)
    {
        cv.visitOuterClass(s, s1, s2);
    }

    public AnnotationVisitor visitAnnotation(String s, boolean flag)
    {
        return cv.visitAnnotation(s, flag);
    }

    public void visitAttribute(Attribute attribute)
    {
        cv.visitAttribute(attribute);
    }

    public void visitInnerClass(String s, String s1, String s2, int i)
    {
        cv.visitInnerClass(s, s1, s2, i);
    }

    public FieldVisitor visitField(int i, String s, String s1, String s2, Object obj)
    {
        return cv.visitField(i, s, s1, s2, obj);
    }

    public MethodVisitor visitMethod(int i, String s, String s1, String s2, String as[])
    {
        return new MethodAdapter(cv.visitMethod(i, s, s1, s2, as));
    }

    public void visitEnd()
    {
        cv.visitEnd();
    }

    protected ClassVisitor cv;
}
