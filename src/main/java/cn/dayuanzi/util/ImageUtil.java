package cn.dayuanzi.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import cn.dayuanzi.config.Settings;
import cn.dayuanzi.exception.GeneralLogicException;


/**
 * 
 * @author : leihc
 * @date : 2015年5月21日
 * @version : 1.0
 */
public class ImageUtil {

	/**
	 * 保存到头像目录的图片
	 * 
	 * @param userId
	 * @param saveFileName
	 * @param multipartFile
	 * @return
	 */
	public static String saveImageInHeadDire(long userId, String saveFileName, CommonsMultipartFile multipartFile){
		if(!YardUtils.allowImage(saveFileName)){
			throw new GeneralLogicException("图片格式只支持"+YardUtils.allowExts);
		}
		File userDire = new File(Settings.HEAD_ICON_DIRE, String.valueOf(userId));
		if(!userDire.exists()){
			userDire.mkdir();
		}
		File localFile = new File(userDire, saveFileName);
		StringBuilder imagePath = new StringBuilder();
		try{
			multipartFile.transferTo(localFile);
			imagePath.append(Settings.IMAGES_DIRETORY.getName()).append("/");
			imagePath.append(Settings.HEAD_ICON_DIRE.getName()).append("/");
			imagePath.append(userDire.getName()).append("/");
			if(saveFileName.endsWith("gif")){
				String toFileName = localFile.getName().replace(".gif", "_c.gif");
				imageCompress(localFile,toFileName);
				imagePath.append(toFileName);
			}else{
				imagePath.append(localFile.getName());
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return imagePath.toString();
	}
	 /**
	  * 保存意见反馈图片
	  */
	public static List<String> saveUserBackImage(long userId, File savePath, CommonsMultipartFile[] multipartFiles){
		List<String> imageNames = new ArrayList<String>();
		int index = 0;
		for(CommonsMultipartFile image : multipartFiles){
			if(image.getSize()<=0){
				continue;
			}
			long currentTime = System.currentTimeMillis();
			String today = DateTimeUtil.formatDateTime(System.currentTimeMillis(), "yyyyMMdd");
			File todayDire = new File(savePath, today);
			if(!todayDire.exists()){
				todayDire.mkdir();
			}
			String originalName = image.getOriginalFilename();
			if(!YardUtils.allowImage(originalName)){
				throw new GeneralLogicException("图片格式只支持"+YardUtils.allowExts);
			}
			String fileName = userId+"_"+String.valueOf(currentTime)+"_"+index+originalName.substring(originalName.lastIndexOf('.'));
			File localFile = new File(todayDire, fileName);
			try{
				image.transferTo(localFile);
				// 保存的图片地址格式：eg:20150421/1111_12323232.jpg
//				imageName.add(today+File.separator+fileName);
				StringBuilder imagePath = new StringBuilder();
				imagePath.append(Settings.IMAGES_DIRETORY.getName()).append("/");
				imagePath.append(Settings.BACK_IMAGES.getName()).append("/");
				imagePath.append(savePath.getName()).append("/");
				imagePath.append(todayDire.getName()).append("/");
				if(fileName.endsWith("gif")){
					String toFileName = localFile.getName().replace(".gif", "_c.gif");
					imageCompress(localFile,toFileName);
					imagePath.append(toFileName);
				}else{
					imagePath.append(localFile.getName());
				}
				imageNames.add(imagePath.toString());
			}catch(Exception ex){
				ex.printStackTrace();
			}
			index++;
		}
		return imageNames;
	
	}
	/**
	 * 保存帖子相关图片
	 * 
	 * @param targetId
	 * @param savePath
	 * @param multipartFiles
	 * @return
	 */
	public static List<String> savePostImage(long targetId, File savePath, CommonsMultipartFile[] multipartFiles){
		List<String> imageNames = new ArrayList<String>();
		int index = 0;
		for(CommonsMultipartFile image : multipartFiles){
			if(image.getSize()<=0){
				continue;
			}
			long currentTime = System.currentTimeMillis();
			String today = DateTimeUtil.formatDateTime(System.currentTimeMillis(), "yyyyMMdd");
			File todayDire = new File(savePath, today);
			if(!todayDire.exists()){
				todayDire.mkdir();
			}
			String originalName = image.getOriginalFilename();
			if(!YardUtils.allowImage(originalName)){
				throw new GeneralLogicException("图片格式只支持"+YardUtils.allowExts);
			}
			String fileName = targetId+"_"+String.valueOf(currentTime)+"_"+index+originalName.substring(originalName.lastIndexOf('.'));
			File localFile = new File(todayDire, fileName);
			try{
				image.transferTo(localFile);
				// 保存的图片地址格式：eg:20150421/1111_12323232.jpg
//				imageName.add(today+File.separator+fileName);
				StringBuilder imagePath = new StringBuilder();
				imagePath.append(Settings.IMAGES_DIRETORY.getName()).append("/");
				imagePath.append(savePath.getName()).append("/");
				imagePath.append(todayDire.getName()).append("/");
				if(fileName.endsWith("gif")){
					String toFileName = localFile.getName().replace(".gif", "_c.gif");
					imageCompress(localFile,toFileName);
					imagePath.append(toFileName);
				}else{
					imagePath.append(localFile.getName());
				}
				imageNames.add(imagePath.toString());
			}catch(Exception ex){
				ex.printStackTrace();
			}
			index++;
		}
		return imageNames;
	}
	
	public static List<String> saveImage(CommonsMultipartFile[] multipartFiles){
		List<String> imageNames = new ArrayList<String>();
		int index = 0;
		for(CommonsMultipartFile image : multipartFiles){
			if(image.getSize()<=0){
				continue;
			}
			long currentTime = System.currentTimeMillis();
			String today = DateTimeUtil.formatDateTime(System.currentTimeMillis(), "yyyyMMdd");
			File todayDire = new File(Settings.GENERAL_IMAGE_DIRE, today);
			if(!todayDire.exists()){
				todayDire.mkdir();
			}
			String originalName = image.getOriginalFilename();
			if(!YardUtils.allowImage(originalName)){
				throw new GeneralLogicException("图片格式只支持"+YardUtils.allowExts);
			}
			int random = new Random().nextInt(1000);
			String fileName = random+"_"+String.valueOf(currentTime)+"_"+index+originalName.substring(originalName.lastIndexOf('.'));
			File localFile = new File(todayDire, fileName);
			try{
				image.transferTo(localFile);
				// 保存的图片地址格式：eg:20150421/1111_12323232.jpg
//				imageName.add(today+File.separator+fileName);
				StringBuilder imagePath = new StringBuilder();
				imagePath.append(Settings.IMAGES_DIRETORY.getName()).append("/");
				imagePath.append(Settings.GENERAL_IMAGE_DIRE.getName()).append("/");
				imagePath.append(todayDire.getName()).append("/");
				if(fileName.endsWith("gif")){
					String toFileName = localFile.getName().replace(".gif", "_c.gif");
					imageCompress(localFile,toFileName);
					imagePath.append(toFileName);
				}else{
					imagePath.append(localFile.getName());
				}
				imageNames.add(imagePath.toString());
			}catch(Exception ex){
				ex.printStackTrace();
			}
			index++;
		}
		return imageNames;
	}
	
	/**
	 * 压缩图片
	 * 
	 * @param srcFile 压缩前的图片
	 * @param toFileName 压缩后的图片
	 */
	public static void imageCompress(File srcFile, String toFileName) {
        try {
            Image image = javax.imageio.ImageIO.read(srcFile);
            int imageWidth = image.getWidth(null);
            int imageHeight = image.getHeight(null);

            // Make a BufferedImage from the Image.
            BufferedImage mBufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = mBufferedImage.createGraphics();
            g2.drawImage(image, 0, 0, imageWidth, imageHeight, null);
            g2.dispose();

            FileOutputStream out = new FileOutputStream(new File(srcFile.getParentFile(),toFileName));

            //通过JPEG编码保存图片
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(mBufferedImage);
            param.setQuality(0.5f, true);// 默认0.75
            encoder.setJPEGEncodeParam(param);
            encoder.encode(mBufferedImage);
            out.close();
        } catch (FileNotFoundException fnf) {
        } catch (IOException ioe) {
            //ioe.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
	/**
     * 描述：
     *
     * @param path
     *            需要压缩的图片路径
     * @param fileName
     *            要压缩的图片名称
     * @param toFileName
     *            压缩后的图片名称
     * @param scale
     *            压缩比例 不能大于1,默认0.5
     * @param quality
     *            压缩品质介于0.1~1.0之间
     * @param width
     *            压缩后的图片的宽度
     * @param height
     *            压缩后的图片的高度 返回值：void
     */
    public static void imageCompress(String path, String fileName,String toFileName, float scale, float quality, int width, int height) {
        try { // 原图路径 原图名称 目标路径 压缩比率0.5 0.75 原图宽度 原图高度
            long start = System.currentTimeMillis();
            Image image = javax.imageio.ImageIO.read(new File(path + fileName));
            int imageWidth = image.getWidth(null);
            int imageHeight = image.getHeight(null);
            if (scale > 0.5)
                scale = 0.5f;// 默认压缩比为0.5，压缩比越大，对内存要去越高，可能导致内存溢出
            // 按比例计算出来的压缩比
            float realscale = getRatio(imageWidth, imageHeight, width, height);
            float finalScale = Math.min(scale, realscale);// 取压缩比最小的进行压缩
            imageWidth = (int) (finalScale * imageWidth);
            imageHeight = (int) (finalScale * imageHeight);
            System.out.println(">>>1:"+(System.currentTimeMillis()-start));

            image = image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_AREA_AVERAGING);
            // Make a BufferedImage from the Image.
            BufferedImage mBufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = mBufferedImage.createGraphics();

            System.out.println(">>>2:"+(System.currentTimeMillis()-start));
//            g2.drawImage(image, 0, 0, imageWidth, imageHeight, Color.white, null);
            g2.drawImage(image, 0, 0, imageWidth, imageHeight, null);
            System.out.println(">>>11:"+(System.currentTimeMillis()-start));
            g2.dispose();

            System.out.println(">>>12:"+(System.currentTimeMillis()-start));
            float[] kernelData2 = { -0.125f, -0.125f, -0.125f, -0.125f, 2, -0.125f, -0.125f, -0.125f, -0.125f };
            Kernel kernel = new Kernel(3, 3, kernelData2);
            ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
            mBufferedImage = cOp.filter(mBufferedImage, null);

            System.out.println(">>>3:"+(System.currentTimeMillis()-start));
            FileOutputStream out = new FileOutputStream(path + toFileName);
            System.out.println(path + toFileName);

            //通过JPEG编码保存图片
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(mBufferedImage);
            param.setQuality(quality, true);// 默认0.75
            encoder.setJPEGEncodeParam(param);
            encoder.encode(mBufferedImage);
            System.out.println(">>>4:"+(System.currentTimeMillis()-start));
            out.close();
            long end = System.currentTimeMillis();
            System.out.println("图片：" + fileName + "，压缩时间：" + (end - start) + "ms");
        } catch (FileNotFoundException fnf) {
        } catch (IOException ioe) {
            
            //ioe.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void imageCompress(String path, String fileName, String toFileName, float scale, int width, int height) {
        imageCompress(path, fileName, toFileName, scale, 0.75f, width, height);
    }

    private static float getRatio(int width, int height, int maxWidth, int maxHeight) {// 获得压缩比率的方法
        float Ratio = 1.0f;
        float widthRatio = (float) maxWidth / width;
        float heightRatio = (float) maxHeight / height;
        
        if (widthRatio < 1.0 || heightRatio < 1.0) {
            Ratio = widthRatio <= heightRatio ? widthRatio : heightRatio;
        }
        return Ratio;
    }

    public static byte[] convertImage2Type(String imageFile, String imageType) throws Exception {// 图片格式转换
        File inputFile = new File(imageFile);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        BufferedImage input = ImageIO.read(inputFile);
        ImageIO.write(input, imageType, output);
        return output.toByteArray();
    }

    public static void convertImage2TypePng(String imageFile, String imageType) throws Exception {// 图片格式转换
        File inputFile = new File(imageFile);
        int suffixIndex = imageFile.lastIndexOf(".");
        String suffix = imageFile.substring(suffixIndex + 1);
        if (!imageType.equals(suffix)) {// 如果原图片的不是PNG格式的图片
            String fileName = imageFile.substring(0, suffixIndex + 1) + imageType;
            File output = new File(fileName);
            BufferedImage input = ImageIO.read(inputFile);
            ImageIO.write(input, imageType, output);
            // 转换后删除原文件
//            if (inputFile.exists())
//                inputFile.delete();
        }
    }
    
    public static void main(String[] args) throws Exception{
//    	convertImage2TypePng("E:\\logs\\11.jpg","png");
//    	imageCompress(new File("e:\\logs\\test.png"), "test3.png");
    	File localFile = new File("e:\\logs\\test.png");
    	
    	imageCompress(localFile,localFile.getName().replace(".png", "_c.png"));
	}
}
