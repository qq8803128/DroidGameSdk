package droid.game.core.parameter;

import droid.game.annotation.Explain;

public class PayParam extends RoleParam<PayParam>{
    @Explain(description = "上游SDK订单id")
    public static final String PAY_ORDER_ID                         = "cpOid";       
    @Explain(description = "商品ID")
    public static final String PAY_PRODUCT_ID                       = "productId";  
    @Explain(description = "商品名称")
    public static final String PAY_PRODUCT_NAME                     = "productName";         
    @Explain(description = "商品描述")
    public static final String PAY_PRODUCT_DESCRIPTION              = "productDescription";  
    @Explain(description = "商品总价")
    public static final String PAY_PRODUCT_AMOUNT                   = "fee";                     
    @Explain(description = "购买数量")
    public static final String PAY_BUY_COUNT                        = "buyCount";              
    @Explain(description = "通知回调地址")
    public static final String PAY_NOTIFY_URL                       = "cpNotifyUrl";           
    @Explain(description = "透传参数，将原封不动返回给CP")
    public static final String PAY_EXTENSION                        = "cpExtension";      

    public PayParam setOrderId(String orderId){
        put(PAY_ORDER_ID,orderId);
        return this;
    }

    public String getOrderId(){
        return get(PAY_ORDER_ID,"");
    }

    public PayParam setProductId(String productId){
        put(PAY_PRODUCT_ID,productId);
        return this;
    }

    public String getProductId(){
        return get(PAY_PRODUCT_ID,"");
    }

    public PayParam setProductName(String productName){
        put(PAY_PRODUCT_NAME,productName);
        return this;
    }

    public String getProductName(){
        return get(PAY_PRODUCT_NAME,"");
    }

    public PayParam setProductDescription(String productDescription){
        put(PAY_PRODUCT_DESCRIPTION,productDescription);
        return this;
    }

    public String getProductDescription(){
        return get(PAY_PRODUCT_DESCRIPTION,"");
    }

    public PayParam setProductAmount(String productAmount){
        put(PAY_PRODUCT_AMOUNT,productAmount);
        return this;
    }

    public String getProductAmount(){
        return get(PAY_PRODUCT_AMOUNT,"");
    }

    public PayParam setBuyCount(int buyCount){
        put(PAY_BUY_COUNT,buyCount);
        return this;
    }

    public int getBuyCount(){
        return get(PAY_BUY_COUNT,1);
    }

    public PayParam setNotifyUrl(String notifyUrl){
        put(PAY_NOTIFY_URL,notifyUrl);
        return this;
    }

    public String getNotifyUrl(){
        return get(PAY_NOTIFY_URL,"");
    }

    public PayParam setExtension(String extension){
        put(PAY_EXTENSION,extension);
        return this;
    }

    public String getExtension(){
        return get(PAY_EXTENSION,"");
    }
}
