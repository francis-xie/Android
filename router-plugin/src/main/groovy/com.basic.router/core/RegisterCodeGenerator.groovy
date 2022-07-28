
package com.basic.router.core

import com.basic.router.util.ScanSetting
import com.basic.router.util.Logger
import org.apache.commons.io.IOUtils
import org.objectweb.asm.*

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

/**
 * 向 LogisticsCenter.class 的 loadRouterMap 中 注入路由注册的代码
 */
class RegisterCodeGenerator {
    ScanSetting extension

    private RegisterCodeGenerator(ScanSetting extension) {
        this.extension = extension
    }

    /**
     * 插入路由注册代码
     * @param registerSetting 扫描到的注册内容
     */
    static void insertInitCodeTo(ScanSetting registerSetting) {
        if (registerSetting != null && !registerSetting.classList.isEmpty()) {
            RegisterCodeGenerator processor = new RegisterCodeGenerator(registerSetting)
            File file = RegisterTransform.fileContainsInitClass
            if (file.getName().endsWith('.jar')) {
                processor.insertInitCodeIntoJarFile(file)
            }
        }
    }

    /**
     * 遍历jar包找到 LogisticsCenter.class 文件，向其中加入注册的代码
     * @param jarFile the jar file which contains LogisticsCenter.class
     * @return
     */
    private File insertInitCodeIntoJarFile(File jarFile) {
        if (jarFile) {
            def optJar = new File(jarFile.getParent(), jarFile.name + ".opt")
            if (optJar.exists()) {
                optJar.delete()
            }

            def file = new JarFile(jarFile)
            Enumeration enumeration = file.entries()
            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(optJar))

            //遍历jar包，找到 LogisticsCenter.class 文件
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) enumeration.nextElement()
                String entryName = jarEntry.getName()
                ZipEntry zipEntry = new ZipEntry(entryName)
                InputStream inputStream = file.getInputStream(jarEntry)
                jarOutputStream.putNextEntry(zipEntry)
                if (ScanSetting.GENERATE_TO_CLASS_FILE_NAME == entryName) {

                    Logger.i('Insert init code to class >> ' + entryName)

                    def bytes = referHackWhenInit(inputStream)
                    jarOutputStream.write(bytes)
                } else {
                    jarOutputStream.write(IOUtils.toByteArray(inputStream))
                }
                inputStream.close()
                jarOutputStream.closeEntry()
            }
            jarOutputStream.close()
            file.close()

            if (jarFile.exists()) {
                jarFile.delete()
            }
            optJar.renameTo(jarFile)
        }
        return jarFile
    }

    /**
     * 访问class类，动态添加执行方法代码
     * @param inputStream
     * @return
     */
    private byte[] referHackWhenInit(InputStream inputStream) {
        ClassReader cr = new ClassReader(inputStream)
        ClassWriter cw = new ClassWriter(cr, 0)
        ClassVisitor cv = new MyClassVisitor(Opcodes.ASM5, cw)
        cr.accept(cv, ClassReader.EXPAND_FRAMES)
        return cw.toByteArray()
    }

    class MyClassVisitor extends ClassVisitor {

        MyClassVisitor(int api, ClassVisitor cv) {
            super(api, cv)
        }

        void visit(int version, int access, String name, String signature,
                   String superName, String[] interfaces) {
            super.visit(version, access, name, signature, superName, interfaces)
        }

        @Override
        MethodVisitor visitMethod(int access, String name, String desc,
                                  String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions)
            //generate code into this method
            if (name == ScanSetting.GENERATE_TO_METHOD_NAME) { //找到动态生成注册代码需要注入的 loadRouterMap 方法
                mv = new RouteMethodVisitor(Opcodes.ASM5, mv)
            }
            return mv
        }
    }

    class RouteMethodVisitor extends MethodVisitor {

        RouteMethodVisitor(int api, MethodVisitor mv) {
            super(api, mv)
        }

        @Override
        void visitInsn(int opcode) {
            //generate code before return
            if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {
                extension.classList.each { name ->
                    name = name.replaceAll("/", ".")  //将类文件的路径转化为包的路径
                    mv.visitLdcInsn(name) //访问方法的参数--搜索到的接口类名
                    // 生成注册代码到 LogisticsCenter.loadRouterMap() 方法中
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC   //操作码
                            , ScanSetting.GENERATE_TO_CLASS_NAME //访问类的类名
                            , ScanSetting.REGISTER_METHOD_NAME //访问的方法
                            , "(Ljava/lang/String;)V"   //访问参数的类型
                            , false)   //访问的类是否是接口
                }
            }
            super.visitInsn(opcode)
        }

        @Override
        void visitMaxs(int maxStack, int maxLocals) {
            super.visitMaxs(maxStack + 4, maxLocals)
        }
    }
}