package com.woting.common.bean;

import com.j256.ormlite.table.DatabaseTable;
import com.woting.common.database.DBUtils;

import java.io.Serializable;

/**
 * Created by amine on 2017/6/14.
 */
@DatabaseTable(tableName = DBUtils.TB_DOWNLOAD)
public class SinglesDownload  extends  SinglesBase implements Serializable {

}
