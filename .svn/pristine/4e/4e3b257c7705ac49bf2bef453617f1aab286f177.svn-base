package com.image.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class ProductServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private String filePath;
	private String tempPath;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		filePath = config.getInitParameter("filepath");
		tempPath = config.getInitParameter("temppath");
		ServletContext context = getServletContext();
		filePath = context.getRealPath(filePath);
		tempPath = context.getRealPath(tempPath);
		new File(filePath).mkdirs();
		new File(tempPath).mkdirs();
		System.out.println("文件存放目录、临时文件目录准备完毕……:" + filePath + "," + tempPath);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		setHeader(request, response);
		controller(request, response);
	}

	private void controller(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String method = request.getParameter("method");
		if (method != null) {
			if (method.equals("save")) {
				doSaveProductInfo(request, response);
			} else if (method.equals("get")) {
				doGetProductInfo(request, response);
			}
		}
	}

	private void doGetProductInfo(HttpServletRequest request,
			HttpServletResponse response) {

	}

	private void doSaveProductInfo(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String username = request.getParameter("username");
		String productImageFileName = request
				.getParameter("productImageFileName");
		PrintWriter out = response.getWriter();
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			try {
				DiskFileItemFactory diskFactory = new DiskFileItemFactory();
				// threshold 极限、临界值，即硬盘缓存 1M
				diskFactory.setSizeThreshold(4 * 1024);
				// repository 贮藏室，即临时文件目录
				diskFactory.setRepository(new File(tempPath));

				ServletFileUpload upload = new ServletFileUpload(diskFactory);
				// 设置允许上传的最大文件大小 4M
				upload.setSizeMax(4 * 1024 * 1024);
				// 解析HTTP请求消息头
				List<FileItem> fileItems = upload.parseRequest(request);
				Iterator<FileItem> iter = fileItems.iterator();
				while (iter.hasNext()) {
					FileItem item = (FileItem) iter.next();
					if (item.isFormField()) {
						// process a regular form field
						processFormField(item, out);
					} else {
						// Process a file upload
						processUploadFile(item, out);
					}
				}

				out.flush();
				out.close();
			} catch (Exception e) {
				System.out.println("使用 fileupload 包时发生异常 ...");
				e.printStackTrace();
			}
		} else {
			System.out.println("the enctype must be multipart/form-data");
		}
		response.setStatus(2);
	}

	private void processFormField(FileItem item, PrintWriter out) {
		// TODO Auto-generated method stub
		String name = item.getFieldName();
		String value = item.getString();
		System.out.println(name + ":" + value);

	}

	// 处理上传的文件
	private void processUploadFile(FileItem item, PrintWriter out)
			throws Exception {
		// String fieldName = item.getFieldName();
		// String contentType = item.getContentType();
		// boolean isInMemory = item.isInMemory();
		boolean writeToFile = true;
		String fileName = item.getName();
		int index = fileName.lastIndexOf("\\");
		fileName = fileName.substring(index + 1, fileName.length());

		long fileSize = item.getSize();

		if ("".equals(fileName) && fileSize == 0) {
			System.out.println("文件名为空 ...");
			return;
		}
		if (writeToFile) {
			File uploadFile = new File(filePath + "/" + fileName);
			if (!uploadFile.exists()){
				System.out.println(uploadFile.getAbsolutePath());
				uploadFile.createNewFile();
			}
			item.write(uploadFile);
			System.out.println(fileName + " 文件保存完毕 ...");
			System.out.println("文件大小为 ：" + fileSize + "\r\n");
			out.print("{\"success\":true,\"imageSrc\":\"http://tom.com:8080/CrossCookieTest/uploadFile/" +fileName + "\"}");
		} else {
			InputStream uploadedStream = item.getInputStream();
			// 流处理
			//byte[] data = item.get();
			uploadedStream.close();
		}

	}

	private void setHeader(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader(
				"P3P",
				"CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

}
