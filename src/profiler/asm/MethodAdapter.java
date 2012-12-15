// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package profiler.asm;


// Referenced classes of package profiler.asm:
//            MethodVisitor, AnnotationVisitor, Attribute, Label

public class MethodAdapter
    implements MethodVisitor
{

    public MethodAdapter(MethodVisitor methodvisitor)
    {
        mv = methodvisitor;
    }

    public AnnotationVisitor visitAnnotationDefault()
    {
        return mv.visitAnnotationDefault();
    }

    public AnnotationVisitor visitAnnotation(String s, boolean flag)
    {
        return mv.visitAnnotation(s, flag);
    }

    public AnnotationVisitor visitParameterAnnotation(int i, String s, boolean flag)
    {
        return mv.visitParameterAnnotation(i, s, flag);
    }

    public void visitAttribute(Attribute attribute)
    {
        mv.visitAttribute(attribute);
    }

    public void visitCode()
    {
        mv.visitCode();
    }

    public void visitInsn(int i)
    {
        mv.visitInsn(i);
    }

    public void visitIntInsn(int i, int j)
    {
        mv.visitIntInsn(i, j);
    }

    public void visitVarInsn(int i, int j)
    {
        mv.visitVarInsn(i, j);
    }

    public void visitTypeInsn(int i, String s)
    {
        mv.visitTypeInsn(i, s);
    }

    public void visitFieldInsn(int i, String s, String s1, String s2)
    {
        mv.visitFieldInsn(i, s, s1, s2);
    }

    public void visitMethodInsn(int i, String s, String s1, String s2)
    {
        mv.visitMethodInsn(i, s, s1, s2);
    }

    public void visitJumpInsn(int i, Label label)
    {
        mv.visitJumpInsn(i, label);
    }

    public void visitLabel(Label label)
    {
        mv.visitLabel(label);
    }

    public void visitLdcInsn(Object obj)
    {
        mv.visitLdcInsn(obj);
    }

    public void visitIincInsn(int i, int j)
    {
        mv.visitIincInsn(i, j);
    }

    public void visitTableSwitchInsn(int i, int j, Label label, Label alabel[])
    {
        mv.visitTableSwitchInsn(i, j, label, alabel);
    }

    public void visitLookupSwitchInsn(Label label, int ai[], Label alabel[])
    {
        mv.visitLookupSwitchInsn(label, ai, alabel);
    }

    public void visitMultiANewArrayInsn(String s, int i)
    {
        mv.visitMultiANewArrayInsn(s, i);
    }

    public void visitTryCatchBlock(Label label, Label label1, Label label2, String s)
    {
        mv.visitTryCatchBlock(label, label1, label2, s);
    }

    public void visitLocalVariable(String s, String s1, String s2, Label label, Label label1, int i)
    {
        mv.visitLocalVariable(s, s1, s2, label, label1, i);
    }

    public void visitLineNumber(int i, Label label)
    {
        mv.visitLineNumber(i, label);
    }

    public void visitMaxs(int i, int j)
    {
        mv.visitMaxs(i, j);
    }

    public void visitEnd()
    {
        mv.visitEnd();
    }

    protected MethodVisitor mv;
}
