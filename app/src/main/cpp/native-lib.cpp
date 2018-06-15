#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_hjimi_logistics_testtinker_MainActivity_getImi(JNIEnv *env, jobject instance) {

    std::string hello = "Hello Imi";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_hjimi_logistics_testtinker_MainActivity_getNews(JNIEnv *env, jobject instance) {
    std::string hello = "News";
    return env->NewStringUTF(hello.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_hjimi_logistics_testtinker_MainActivity_welcomeImi(JNIEnv *env, jobject instance) {
    std::string welcome = "welcome to Imi";
    return env->NewStringUTF(welcome.c_str());
}