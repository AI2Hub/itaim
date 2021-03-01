package com.asset.management.service;

import com.asset.management.dao.AssetDao;
import com.asset.management.entity.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class AssetService {
    @Autowired
    private AssetDao assetDao;

    /**
     * 根据工号jobNumber查找资产数据
     * @param jobNumber
     * @return
     */
    public List<Asset> findByJobNumber(int jobNumber){
        return assetDao.findByJobNumber(jobNumber);
    }

    /**
     * 根据资产编号查询资产数据
     * @param assetNumber
     * @return
     */
    public List<Asset> findByAssetNumber(String assetNumber){
        return assetDao.findByAssetNumber(assetNumber);
    }
    /**
     * 分页显示资产数据
     * @param page
     * @param size
     * @return
     */
    public Page<Asset> listAsset(Integer page, Integer size){
        Pageable pageable = PageRequest.of(page,size);
        return assetDao.findAll(pageable);
    }

    /**
     * 根据状态查询数据，查询结果分页显示
     * Sort.Direction.ASC,"id" 排序方式
     * @param status 查询条件
     * @param page 页数
     * @param size 每页数据条数
     * @return
     */
    public Page<Asset>listByStatus(String status, Integer page,Integer size){
        Pageable pageable = PageRequest.of(page,size, Sort.Direction.ASC,"id");
        Page<Asset> data = assetDao.findByStatus(status, pageable);
        return data;
    }

    /**
     * 根据id删除资产数据
     * @param id
     */
    public void deleteAsset(int id){
        assetDao.deleteById(id);
    }

    /**
     * 批量删除
     * @param ids 删除对象id组成的对象组
     * @return
     */
    public String bathDeleteAsset(String ids){
        List idList = Arrays.asList(ids.split(","));
        idList.forEach(id ->{
            Integer assetId = Integer.parseInt((String) id);
            assetDao.deleteById(assetId);
        });
        return "success";
    }

    /**
     * 根据Ids查找资产数据
     * @param ids 批量添加的id
     * @return
     */
    public List<Asset> bathFindAsset(String ids){
        List idList = Arrays.asList(ids.split(","));
        List<Asset> list = new ArrayList<>();
        idList.forEach(id ->{
            int assetId = Integer.parseInt((String) id);
            list.add(assetDao.findById(assetId));
        });
        return list;
    }

    /**
     * 编辑资产信息
     * @param asset
     */
    public Asset updateAsset(Asset asset){
        return assetDao.save(asset);
    }

    /**
     * 修改资产数据入库状态
     * @param ids
     * @return
     */
    public void updateAssetStatus(String ids){
        List idList = Arrays.asList(ids.split(","));
        idList.forEach(id ->{
            int assetId = Integer.parseInt((String) id);
            Asset asset = assetDao.findById(assetId);
            asset.setStatus("是");
            assetDao.save(asset);
        });
    }
}
