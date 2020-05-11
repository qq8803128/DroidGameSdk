package droid.game.plugin.sdk.delegate.db.entity;

import droid.game.open.source.orm.db.annotation.Column;
import droid.game.open.source.orm.db.annotation.PrimaryKey;
import droid.game.open.source.orm.db.annotation.Table;
import droid.game.open.source.orm.db.annotation.Unique;
import droid.game.open.source.orm.db.enums.AssignType;

@Table("userTable")
public class UserEntity {
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    @Column("__id")
    private int mId;

    @Unique
    @Column("unique_id")
    private String mUniqueId;

    @Column("open_id")
    private String mOpenId;

    @Column("channel_name")
    private String mChannelName;

    @Column("uid")
    private String mUid;

    @Column("token")
    private String token;

    @Column("name")
    private String mName;

    @Column("password")
    private String mPassword;

    @Column("timestamp")
    private long mTimeStamp;

    @Column("role")
    private String mRole;

    @Column("server")
    private String mServer;

    @Column("ext1")
    private String mExt1;

    @Column("ext2")
    private String mExt2;

    @Column("ext3")
    private String mExt3;

    @Column("ext4")
    private String mExt4;

    @Column("ext5")
    private String mExt5;

    public String getUniqueId() {
        return mUniqueId;
    }

    public UserEntity setUniqueId(String uniqueId) {
        mUniqueId = uniqueId;
        return this;
    }

    public String getOpenId() {
        return mOpenId;
    }

    public UserEntity setOpenId(String openId) {
        mOpenId = openId;
        return this;
    }

    public String getChannelName() {
        return mChannelName;
    }

    public UserEntity setChannelName(String channelName) {
        mChannelName = channelName;
        return this;
    }

    public String getUid() {
        return mUid;
    }

    public UserEntity setUid(String uid) {
        mUid = uid;
        return this;
    }

    public String getToken() {
        return token;
    }

    public UserEntity setToken(String token) {
        this.token = token;
        return this;
    }

    public String getName() {
        return mName;
    }

    public UserEntity setName(String name) {
        mName = name;
        return this;
    }

    public String getPassword() {
        return mPassword;
    }

    public UserEntity setPassword(String password) {
        mPassword = password;
        return this;
    }

    public long getTimeStamp() {
        return mTimeStamp;
    }

    public UserEntity setTimeStamp(long timeStamp) {
        mTimeStamp = timeStamp;
        return this;
    }

    public String getRole() {
        return mRole;
    }

    public UserEntity setRole(String role) {
        mRole = role;
        return this;
    }

    public String getServer() {
        return mServer;
    }

    public UserEntity setServer(String server) {
        mServer = server;
        return this;
    }

    public String getExt1() {
        return mExt1;
    }

    public UserEntity setExt1(String ext1) {
        mExt1 = ext1;
        return this;
    }

    public String getExt2() {
        return mExt2;
    }

    public UserEntity setExt2(String ext2) {
        mExt2 = ext2;
        return this;
    }

    public String getExt3() {
        return mExt3;
    }

    public UserEntity setExt3(String ext3) {
        mExt3 = ext3;
        return this;
    }

    public String getExt4() {
        return mExt4;
    }

    public UserEntity setExt4(String ext4) {
        mExt4 = ext4;
        return this;
    }

    public String getExt5() {
        return mExt5;
    }

    public UserEntity setExt5(String ext5) {
        mExt5 = ext5;
        return this;
    }
}
