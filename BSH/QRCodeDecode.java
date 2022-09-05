import android.content.Context;
import dalvik.system.DexClassLoader;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Yingy.
 * Name: 二维码解码
 * QQ: 2665501650
 */
public String decode(String url) {
	DexClassLoader dexClassLoader = new DexClassLoader("/storage/emulated/0/QR/QRDic/BSH/decoder.dex", context.getCacheDir().getAbsolutePath(), null, context.getClassLoader());
	try {
		Class clazz = dexClassLoader.loadClass("com.Yingy.QRDicLib.DeQRCode.Decode");
		try {
			Method method = clazz.getMethod("syncDecodeQRCode", new Class[]{java.lang.String.class});
			try {
				Object result = method.invoke(null, new String[]{url});
				if(result != null) {
					return result.toString();
				}
			} catch (InvocationTargetException e) {} catch (IllegalArgumentException e) {} catch (IllegalAccessException e) {}
		} catch (NoSuchMethodException e) {} catch (SecurityException e) {}
	} catch (ClassNotFoundException e) {}
	return "";
}