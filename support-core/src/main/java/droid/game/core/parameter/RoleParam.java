package droid.game.core.parameter;

import android.text.TextUtils;
import droid.game.annotation.Explain;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RoleParam<TO extends RoleParam> extends Parameter<TO>{
    @Explain(description = "提交角色类型")
    public static final String ROLE_SUBMIT_TYPE                     = "submitType";                
    @Explain(description = "上游用户ID")
    public static final String ROLE_CP_USER_ID                      = "cpUid";                    
    @Explain(description = "角色ID")
    public static final String ROLE_ID                              = "roleId";                    
    @Explain(description = "角色名")
    public static final String ROLE_NAME                            = "roleName";                 
    @Explain(description = "角色等级")
    public static final String ROLE_LEVEL                           = "roleLevel";                
    @Explain(description = "角色性别")
    public static final String ROLE_SEX                             = "roleSex";                  
    @Explain(description = "角色职业ID")
    public static final String ROLE_PROFESSION_ID                   = "roleProfessionId";        
    @Explain(description = "角色职业名")
    public static final String ROLE_PROFESSION_NAME                 = "roleProfessionName";        
    @Explain(description = "角色战斗力")
    public static final String ROLE_POWER                           = "rolePower";                 
    @Explain(description = "服务器ID")
    public static final String ROLE_SERVER_ID                       = "serverId";                
    @Explain(description = "服务器名称")
    public static final String ROLE_SERVER_NAME                     = "serverName";             
    @Explain(description = "VIP等级")
    public static final String ROLE_VIP_LEVEL                       = "vipLevel";                   
    @Explain(description = "角色身上虚拟币(需要通过充值获取，元宝、点券、钻石等)数量")
    public static final String ROLE_TICKET_COUNT                    = "RoleTicketCount";         
    @Explain(description = "角色身上游戏币数量")
    public static final String ROLE_MONEY_COUNT                     = "RoleMoneyCount";           
    @Explain(description = "工会ID")
    public static final String ROLE_UNION_ID                        = "unionId";                   
    @Explain(description = "工会名称")
    public static final String ROLE_UNION_NAME                      = "unionName";               
    @Explain(description = "创建角色时间,13位时间戳")
    public static final String ROLE_CREATE_TIME                     = "roleCreatedAt";            

    public static final int SUBMIT_ERR = 0;

    @Explain(description = "进入游戏")
    public static final int ENTER_GAME = 1;

    @Explain(description = "角色升级")
    public static final int LEVEL_UP = 2;

    @Explain(description = "进入副本")
    public static final int ENTER_COPY = 3;

    @Explain(description = "离开副本")
    public static final int LEAVE_COPY = 4;

    @Explain(description = "创建角色")
    public static final int CREATE_ROLE = 100;

    public TO setRoleSubmitType(int type){
        put(ROLE_SUBMIT_TYPE,type);
        return (TO) this;
    }

    public TO setCpUserId(String cpUserId){
        put(ROLE_CP_USER_ID,cpUserId);
        return (TO) this;
    }

    public TO setRoleId(String roleId){
        put(ROLE_ID,roleId);
        return (TO) this;
    }

    public TO setRoleName(String roleName){
        put(ROLE_NAME,roleName);
        return (TO) this;
    }

    public TO setRoleLevel(String roleLevel){
        put(ROLE_LEVEL,roleLevel);
        return (TO) this;
    }

    public TO setRoleSex(String roleSex){
        put(ROLE_SEX,replaceNullAttr(roleSex,"未知"));
        return (TO) this;
    }

    public TO setRoleProfessionId(String roleProfessionId){
        put(ROLE_PROFESSION_ID,replaceNullAttr(roleProfessionId,"0"));
        return (TO) this;
    }

    public TO setRoleProfessionName(String roleProfessionName){
        put(ROLE_PROFESSION_NAME,replaceNullAttr(roleProfessionName,"未知"));
        return (TO) this;
    }

    public TO setRolePower(String rolePower){
        put(ROLE_POWER,replaceNullAttr(rolePower,"0"));
        return (TO) this;
    }

    public TO setServerId(String serverId){
        put(ROLE_SERVER_ID,serverId);
        return (TO) this;
    }

    public TO setServerName(String serverName){
        put(ROLE_SERVER_NAME,serverName);
        return (TO) this;
    }

    public TO setVipLevel(String vipLevel){
        put(ROLE_VIP_LEVEL,replaceNullAttr(vipLevel,"0"));
        return (TO) this;
    }

    public TO setRoleTicketCount(String roleTicketCount){
        put(ROLE_TICKET_COUNT,replaceNullAttr(roleTicketCount,"0"));
        return (TO) this;
    }

    public TO setRoleMoneyCount(String roleMoneyCount){
        put(ROLE_MONEY_COUNT,replaceNullAttr(roleMoneyCount,"0"));
        return (TO) this;
    }

    public TO setUnionId(String unionId){
        put(ROLE_UNION_ID,replaceNullAttr(unionId,"0"));
        return (TO) this;
    }

    public TO setUnionName(String unionName){
        put(ROLE_UNION_NAME,replaceNullAttr(unionName,"暂无"));
        return (TO) this;
    }

    public RoleParam setCreateRoleTime(long createRoleTime){
        put(ROLE_CREATE_TIME, convertTimeStamp(createRoleTime));
        return this;
    }

    public int getRoleSubmitType(){
        return get(ROLE_SUBMIT_TYPE,SUBMIT_ERR);
    }

    public String getCpUserId(){
        return get(ROLE_CP_USER_ID,"");
    }

    public String getRoleId(){
        return get(ROLE_ID,"");
    }

    public String getRoleName(){
        return get(ROLE_NAME,"");
    }

    public String getRoleLevel(){
        return get(ROLE_LEVEL,"");
    }

    public String getRoleSex(){
        return get(ROLE_SEX,"");
    }

    public String getRoleProfessionId(){
        return get(ROLE_PROFESSION_ID,"");
    }

    public String getRoleProfessionName(){
        return get(ROLE_PROFESSION_NAME,"");
    }

    public String getRolePower(){
        return get(ROLE_POWER,"");
    }

    public String getServerId(){
        return get(ROLE_SERVER_ID,"");
    }

    public String getServerName(){
        return get(ROLE_SERVER_NAME,"");
    }

    public String getVipLevel(){
        return get(ROLE_VIP_LEVEL,"");
    }

    public String getRoleTicketCount(){
        return get(ROLE_TICKET_COUNT,"");
    }

    public String getRoleMoneyCount(){
        return get(ROLE_MONEY_COUNT,"");
    }

    public String getUnionId(){
        return get(ROLE_UNION_ID,"");
    }

    public String getUnionName(){
        return get(ROLE_UNION_NAME,"");
    }

    public String getCreateRoleTime(){
        return get(ROLE_CREATE_TIME,"");
    }

    private String replaceNullAttr(String value,String defVal){
        if (TextUtils.isEmpty(value)){
            return defVal;
        }
        return value;
    }
    
    public static String convertTimeStamp(long time) {
        try {
            String t = time + "";
            if (t.length() == 10) {
                t = t + "000";
            }
            String format = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(new Date(Long.parseLong(t)));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "未知";
    }
}
