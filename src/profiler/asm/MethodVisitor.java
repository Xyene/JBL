// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package profiler.asm;


// Referenced classes of package profiler.asm:
//            AnnotationVisitor, Attribute, Label

public interface MethodVisitor
{

    public abstract AnnotationVisitor visitAnnotationDefault();

    public abstract AnnotationVisitor visitAnnotation(String s, boolean flag);

    public abstract AnnotationVisitor visitParameterAnnotation(int i, String s, boolean flag);

    public abstract void visitAttribute(Attribute attribute);

    public abstract void visitCode();

    public abstract void visitInsn(int i);

    public abstract void visitIntInsn(int i, int j);

    public abstract void visitVarInsn(int i, int j);

    public abstract void visitTypeInsn(int i, String s);

    public abstract void visitFieldInsn(int i, String s, String s1, String s2);

    public abstract void visitMethodInsn(int i, String s, String s1, String s2);

    public abstract void visitJumpInsn(int i, Label label);

    public abstract void visitLabel(Label label);

    public abstract void visitLdcInsn(Object obj);

    public abstract void visitIincInsn(int i, int j);

    public abstract void visitTableSwitchInsn(int i, int j, Label label, Label alabel[]);

    public abstract void visitLookupSwitchInsn(Label label, int ai[], Label alabel[]);

    public abstract void visitMultiANewArrayInsn(String s, int i);

    public abstract void visitTryCatchBlock(Label label, Label label1, Label label2, String s);

    public abstract void visitLocalVariable(String s, String s1, String s2, Label label, Label label1, int i);

    public abstract void visitLineNumber(int i, Label label);

    public abstract void visitMaxs(int i, int j);

    public abstract void visitEnd();
}
