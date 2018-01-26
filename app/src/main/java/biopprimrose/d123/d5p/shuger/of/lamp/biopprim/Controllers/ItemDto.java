package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;

/**
 * Created by tsuchiya on 2017/12/21.
 */

public class ItemDto {
    private String name = "";             // 名前
//    private int resourceId = R.drawable.icon_biop001;  // アイコンのResource ID. DefaultはLauncherアイコン

    public ItemDto(String name, int id) {
        this.name = name;
//        this.resourceId = id;
    }

    public ItemDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    //    public int getResourceId() {
//        return resourceId;
//    }
//    public void setResourceId(int resourceId) {
//        this.resourceId = resourceId;
//    }
}
