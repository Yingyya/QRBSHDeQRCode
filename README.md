<center><h3>BSH 二维码解码</h3></center>

本项目使用了[Google Zxing](https://github.com/zxing/zxing)作为主要库

### QRCodeDecode.java 代码
```java
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
```
### Decode.java 代码
```java
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by Yingy.
 * Name: 二维码解码
 * QQ: 2665501650
 */
public class Decode {
	
	public static final Map<DecodeHintType, Object> HINTS = new EnumMap<>(DecodeHintType.class);
	
	public static String syncDecodeQRCode(String url) {
		Bitmap bitmap = getBitmap(url);
		if(bitmap == null) {
			return null;
		}
		Result result = null;
		RGBLuminanceSource source = null;
		try {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			int[] pixels = new int[width * height];
			bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
			source = new RGBLuminanceSource(width, height, pixels);
			result = new MultiFormatReader().decode(new BinaryBitmap(new HybridBinarizer(source)), HINTS);
			return result.getText();
		} catch (Exception e) {
			e.printStackTrace();
			if (source != null) {
				try {
					result = new MultiFormatReader().decode(new BinaryBitmap(new GlobalHistogramBinarizer(source)), HINTS);
					return result.getText();
				} catch (Throwable e2) {
					e2.printStackTrace();
				}
			}
			return null;
		}
	}
	
	private static Bitmap getBitmap(String imgUrl) {
		Bitmap bitmap = null;
		InputStream in=null;
		BufferedOutputStream out = null;
		try {
			in = new BufferedInputStream(new URL(imgUrl).openStream(), 1024);
			final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
			out = new BufferedOutputStream(dataStream, 1024);
			copy(in, out);
			out.flush();
			byte[] data = dataStream.toByteArray();
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			data = null;
			return bitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static void copy(InputStream in, OutputStream out) throws IOException {
		byte[] b = new byte[1024];
		int read;
		while ((read = in.read(b)) != -1) {
			out.write(b, 0, read);
		}
	}
}
```
### 词库代码
```
## 二维码解析 无调用API接口
## Author: Yingy
## 慢的原因的腾讯图床问题 自己提供图片链接很快

二维码解析 ?(.*)
如果:%括号1%!=
正在解析……
$发送$
R:$BSH QRCodeDecode.java decode %括号1%$
如果:%R%==
解析失败……
返回
如果尾
解析成功\r\n
结果：%R%
返回
如果尾
如果:%IMG0%==
请附带图片……
返回
如果尾
M:$正则 € %IMG0%€/.*-|[^\dA-Z]€$
正在解析……
$发送$
R:$BSH QRCodeDecode.java decode https://gchat.qpic.cn/gchatpic_new/0/0-0-%M%/0$
如果:%R%==
解析失败……
返回
如果尾
解析成功\r\n
结果：%R%
```
