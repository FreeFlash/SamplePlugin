package com.tianxiaolei.plugin.lifecycle;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class LifecycleClassVisitor extends ClassVisitor implements Opcodes {

    private String mClassName;

    public LifecycleClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        mClassName = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        //匹配FragmentActivity
        if ("androidx/fragment/app/FragmentActivity".equals(this.mClassName)) {
            if ("onCreate".equals(name)) {
                return new LifecycleOnCreateVisitor(methodVisitor, access, name, descriptor);
            } else if ("onDestory".equals(name)) {
                return new LifecycleOnDestroyVisitor(methodVisitor, access, name, descriptor);
            }
        }
        return methodVisitor;
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}
