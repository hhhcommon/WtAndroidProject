package com.wotingfm.ui.intercom.group.editgroupmessage.model;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class AddressModel {

    private String CatalogType;
    private String CatalogName;
    private String CatalogId;
    private List<SubCata> SubCata;

    public List<AddressModel.SubCata> getSubCata() {
        return SubCata;
    }

    public void setSubCata(List<AddressModel.SubCata> subCata) {
        SubCata = subCata;
    }

    public String getCatalogType() {
        return CatalogType;
    }

    public void setCatalogType(String catalogType) {
        CatalogType = catalogType;
    }

    public String getCatalogName() {
        return CatalogName;
    }

    public void setCatalogName(String catalogName) {
        CatalogName = catalogName;
    }

    public String getCatalogId() {
        return CatalogId;
    }

    public void setCatalogId(String catalogId) {
        CatalogId = catalogId;
    }



    public static class SubCata implements Serializable {
        private String CatalogType;
        private String CatalogName;
        private String CatalogId;

        public String getCatalogType() {
            return CatalogType;
        }

        public void setCatalogType(String catalogType) {
            CatalogType = catalogType;
        }

        public String getCatalogName() {
            return CatalogName;
        }

        public void setCatalogName(String catalogName) {
            CatalogName = catalogName;
        }

        public String getCatalogId() {
            return CatalogId;
        }

        public void setCatalogId(String catalogId) {
            CatalogId = catalogId;
        }
    }
}
