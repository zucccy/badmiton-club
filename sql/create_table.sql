-- 创建库
create
database if not exists badminton_club;
-- 切换库
use
badminton_club;
-- 系统用户表（含管理员）
CREATE TABLE user
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    account     VARCHAR(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',
    `password`  VARCHAR(512) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
    username    VARCHAR(256) COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '用户昵称',
    role        VARCHAR(64) COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT 'user' COMMENT '用户角色：user/admin',
    create_time TIMESTAMP                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP                               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_delete   TINYINT                                 NOT NULL DEFAULT '0' COMMENT '是否删除'
) COMMENT='系统用户表';

-- 会员基本信息表
CREATE TABLE member
(
    id               BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id          BIGINT UNIQUE NOT NULL COMMENT '关联用户ID',
    `name`           VARCHAR(64)   NOT NULL COMMENT '姓名',
    gender           TINYINT       NOT NULL DEFAULT '0' COMMENT '性别，0为男，1为女',
    birth_date       DATE          NOT NULL COMMENT '出生日期',
    phone            VARCHAR(32) COMMENT '手机号码',
    nation           VARCHAR(32) COMMENT '民族',
    origin_address   VARCHAR(256) COMMENT '籍贯',
    home_address     VARCHAR(512)  NOT NULL COMMENT '家庭住址',
    work_unit        VARCHAR(512) COMMENT '工作单位',
    occupation       VARCHAR(64) COMMENT '职业',
    political_party  VARCHAR(64) COMMENT '党派',
    club_duty        VARCHAR(64) COMMENT '俱乐部职务',

    -- 身份标识
    is_civil_servant TINYINT       NOT NULL DEFAULT '0' COMMENT '是否公务员，0为否',
    is_cadre         TINYINT       NOT NULL DEFAULT '0' COMMENT '是否科局级及以上，0为否',
    is_veteran       TINYINT       NOT NULL DEFAULT '0' COMMENT '是否退役军人，0为否',

    -- 运动相关
    athlete_level    VARCHAR(32) COMMENT '专业运动员等级',
    referee_level    VARCHAR(32) COMMENT '裁判员等级',
    honour_info      VARCHAR(1024) COMMENT '荣誉信息',

    -- 身体信息
    height           DECIMAL(5, 2) COMMENT '身高(cm)',
    weight           DECIMAL(5, 2) COMMENT '体重(kg)',
    uniform_size     VARCHAR(16) COMMENT '服装尺寸',

    -- 归属信息
    residence_area   VARCHAR(32)   NOT NULL COMMENT '人员归属地（分龙港、苍南、平阳、温州市内、浙江省内、省外6个选项）',
    current_club_id  BIGINT COMMENT '当前所属俱乐部id',
    current_level    TINYINT       NOT NULL DEFAULT '0' COMMENT '当前组别，0表示丙，1表示乙，2表示甲',

    -- 隐私信息
    id_number        VARCHAR(512) COMMENT '加密身份证号',
    bank_account     VARCHAR(512) COMMENT '加密银行卡号',
    bank_name        VARCHAR(256),
    bank_branch      VARCHAR(256),

    create_time      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'

) COMMENT='会员主表';

-- 俱乐部表
CREATE TABLE club
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    club_name   VARCHAR(256) NOT NULL,
    create_time TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='俱乐部信息';

-- 俱乐部会员联系表
CREATE TABLE club_member_relation
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_id  BIGINT NOT NULL,
    club_id    BIGINT NOT NULL,
    start_date DATE   NOT NULL,
    end_date   DATE COMMENT '为空表示当前所属'

) COMMENT='俱乐部会员联系表';

-- 赛事表
CREATE TABLE game
(
    id               BIGINT PRIMARY KEY AUTO_INCREMENT,
    game_name        VARCHAR(512) NOT NULL,
    game_description VARCHAR(512) NOT NULL COMMENT '赛事简介',
    start_date       DATE         NOT NULL,
    end_date         DATE         NOT NULL
) COMMENT='赛事信息';

-- 积分记录表
CREATE TABLE score_record
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_id BIGINT NOT NULL,
    game_id   BIGINT NOT NULL,
    score     INT    NOT NULL COMMENT '本次赛事获得积分',
    ranking   INT    NOT NULL COMMENT '赛事名次'

) COMMENT='积分记录';

-- 附件表
CREATE TABLE attachment
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_id     BIGINT       NOT NULL,
    file_type     VARCHAR(16)  NOT NULL COMMENT '身份证、房产原件、社保',
    file_path     VARCHAR(500) NOT NULL,
    `status`      ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    reviewed_by   BIGINT COMMENT '审核人',
    reviewed_time TIMESTAMP COMMENT '审核时间',
    upload_time   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间'
) COMMENT='文件附件表';