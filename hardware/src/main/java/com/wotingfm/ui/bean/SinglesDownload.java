package com.wotingfm.ui.bean;

import com.j256.ormlite.table.DatabaseTable;
import com.wotingfm.common.database.DBUtils;

import java.io.Serializable;

/**
 * Created by amine on 2017/6/14.
 */
@DatabaseTable(tableName = DBUtils.TB_DOWNLOAD)
public class SinglesDownload  extends  SinglesBase implements Serializable {

}
