package com.wotingfm.common.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.wotingfm.common.database.DBUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by amine on 2017/6/14.
 */
@DatabaseTable(tableName = DBUtils.TB_DOWNLOAD)
public class SinglesDownload  extends  SinglesBase implements Serializable {

}
