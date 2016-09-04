package com.anyway.imagemark.image;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.UnknownHostException;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;
import javax.imageio.ImageIO;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

public class gridFS {
	MongoClient mClient;
	DB db;
	DBCollection collection;
	String mongoDBHost = "222.214.218.140";
	int mongoDBPort = 27017;
	String dbName = "original";
	String collectionName = "fs";
	GridFS originalFS;
	private static Logger logger = Logger.getLogger(gridFS.class);
	/**
	 * @param image
	 *            图片流
	 * @param fileName
	 *            图片存储名称;
	 * @param x
	 *            剪裁图片起点x;
	 * @param y
	 *            剪裁图片起点y;
	 * @param width
	 *            剪裁宽度
	 * @param height
	 *            剪裁高度
	 */
	public void saveImage(MultipartFile image, String fileName, int x, int y,
			int width, int height) {
		if (image != null) {
			if (image.getSize() > 10000000) {
				logger.info("the picture is too large");
			} else {
				BufferedImage bufferedImage = null;
				try {
					logger.info("read");
					bufferedImage = ImageIO.read(image.getInputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				BufferedImage cropImage = cropBufferedImage(bufferedImage, x,
						y, width, height);
				save(originalFS, cropImage, fileName);
			}
		} else {
			logger.info("the picture is empty ");
		}
	}
	public BufferedImage cropBufferedImage(BufferedImage image, int x, int y,
			int width, int height) {
		BufferedImage bufferedImage = null;
		try {
			logger.info("cropBufferedImage");
			bufferedImage = Thumbnails.of(image)
					.sourceRegion(x, y, width, height).scale(1.0f)
					.asBufferedImage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bufferedImage;
	}

	//
	public void save(GridFS fs, BufferedImage image, String name) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			logger.info("save");
			ImageIO.write(image, "jpeg", os);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStream in = new ByteArrayInputStream(os.toByteArray());
		GridFSInputFile gridFSInputFile = fs.createFile(in);
		gridFSInputFile.setChunkSize(24*1024);
		gridFSInputFile.setFilename(name);
		gridFSInputFile.setContentType("png");
		gridFSInputFile.save();
		return;
	}
	public gridFS() throws UnknownHostException{
		mClient = new MongoClient(mongoDBHost, mongoDBPort);
		db = mClient.getDB(dbName);
		collection = db.getCollection(collectionName);
		originalFS = new GridFS(db);
	}
}