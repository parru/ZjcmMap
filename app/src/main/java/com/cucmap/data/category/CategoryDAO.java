package com.cucmap.data.category;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Xml;

import com.cucmap.data.TBManager;
import com.cucmap.memory.FileManager;
import com.zjcmmap.R;

@SuppressLint("UseSparseArrays")
public class CategoryDAO extends TBManager {
    public static final String TABLE_NAME = "category";
    public static final String FILE_NAME = "Category.xml";
    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String MARK = "mark";
    
    private SQLiteDatabase database;
    public CategoryDAO(SQLiteDatabase database){
        super(database);
        this.database = database;
    }

    @Override
    public void createTable() {
        // TODO Auto-generated method stub
        String sql = "create table " + TABLE_NAME + "(";
        sql += NAME + " CHAR(10) primary key,";
        sql += TYPE + " INTEGER,";
        sql += MARK + " CHAR(10)";
        sql += ")";
        database.execSQL(sql);
    }

    @Override
    public void dropTable() {
        // TODO Auto-generated method stub
        if(tableIsExist(TABLE_NAME)){
            Log.i("drop table",TABLE_NAME);
            String sql = "drop table " + TABLE_NAME;
            database.execSQL(sql);
        }
    }
    
    public void insertElem(Category category){
        if(category == null){
            return;
        }
        ContentValues values = new ContentValues();
        values.put(NAME, category.getName());
        values.put(TYPE, category.getType());
        values.put(MARK, category.getMark());
        database.insert(TABLE_NAME, null, values);
    }

    @Override
    public void updateTable() {
        // TODO Auto-generated method stub
        FileManager fileManager = new FileManager();
        InputStream input = fileManager.readFileFromRAM(fileManager.XML_DIR, FILE_NAME);
        if(input != null){
            dropTable();
            createTable();
            parserXml(input);
        }else{
            Log.i(FILE_NAME,"file is null");
        }
        
    }
    
    /**
     * 根据类型得到部门的分类的图标
     * @param  type type的类型
     * @return id   分类图标的ID
     */
    public static int getBitmapByType(int type){
        int bitmap_id = 0;
        Map<Integer, Integer> item_images = new HashMap<Integer, Integer>();

        item_images.put(1, R.drawable.classroom);
        item_images.put(2, R.drawable.college);
        item_images.put(3, R.drawable.hospital);
        item_images.put(4, R.drawable.dorm);
        item_images.put(5, R.drawable.dining);
        item_images.put(6, R.drawable.school);
        item_images.put(7, R.drawable.coffee);
        item_images.put(8, R.drawable.library);
        item_images.put(9, R.drawable.wc);
        item_images.put(10, R.drawable.atm);
        item_images.put(11, R.drawable.bookshop);
        item_images.put(12, R.drawable.market);
        item_images.put(13,R.drawable.scence);
        
        if(type > 0 && type < 14){
            bitmap_id = (Integer) item_images.get(type);
        }
        else{
            bitmap_id = R.drawable.ic_launcher;
        }
        return bitmap_id;
    }
    
    /**
     * 根据XML数据更新表
     * @param in    XML文件输入流
     */
    public void parserXml(InputStream in) {
        try {
            // 得到文件流，并设置编码方式
            XmlPullParser xmlParser = Xml.newPullParser();
            xmlParser.setInput(in, "UTF-8");
            
            //这里有开始文档，结束文档，开始标签，结束标签，文本等等事件。
            int evtType = xmlParser.getEventType();
            // 一直循环，直到文档结束
            Category category = null;
            while (evtType != XmlPullParser.END_DOCUMENT) {
                switch (evtType) {
                
                case XmlPullParser.START_TAG:  //标签开始
                    
                    String tag = xmlParser.getName();
                    if(tag.equals(TABLE_NAME)){
                        // 如果是user标签开始，则说明需要实例化对象了
                        category = new Category();
                    }
                    
                    if(tag.equalsIgnoreCase(NAME)){
                        // 取出User标签中的一些属性值
                        String name = xmlParser.nextText();
                        category.setName(name);
                    }
                    
                    if(tag.equalsIgnoreCase(TYPE)){
                        String type = xmlParser.nextText();
                        category.setType(Integer.parseInt(type));
                    }
                    
                    if(tag.equalsIgnoreCase(MARK)){
                        String mark = xmlParser.nextText();
                        category.setMark(mark);
                    }                    
                    break;
                    
                case XmlPullParser.END_TAG:
                    // 如果遇到river标签结束，则把river对象添加进集合中
                    if (xmlParser.getName().equals(TABLE_NAME)) {
                        if(category != null){
                            insertElem(category);
                        }
                    }
                    break;                  
                default:
                    break;
                }
                evtType = xmlParser.next();
            }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}
