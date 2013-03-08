package com.bpd.smilemorph;

public class ProjectEntity {

public static String TABLE_NAME = "MorphTb";
	
	public static String ID = "id";
	public static String PROJ_NAME = "morphname";
	public static String IMG_STR = "imagestring";
	public static String IMG_NO = "noimges";
	/*public static String EMP_ADDRESS = "empaddress";*/
	
	public Integer id,img_no;
	public String proj_name, img_str;
	// Empty constructor
    public ProjectEntity(){
 
    }
	public ProjectEntity(int id, String pname, String imgstring,int noimages) {
		this.id = id;
		this.proj_name = pname;
		this.img_str = imgstring;
		//this.emp_address = eaddress;
		this.img_no = noimages;
	}
	// getting ID
    public int getID(){
        return this.id;
    }
 
    // setting id
    public void setID(int id){
        this.id = id;
    }
 
    // getting name
    public String getName(){
        return this.proj_name;
    }
 
    // setting name
    public void setName(String name){
        this.proj_name = name;
    }
 // getting name
    public String getImgString(){
        return this.img_str;
    }
 
    // setting name
    public void setImgString(String imgstring){
        this.img_str = imgstring;
    }
 
    // getting image number
    public int getImgNumber(){
        return this.img_no;
    }
 
    // setting image number
    public void setImgNumber(int noimages){
        this.img_no = noimages;
    }
}
