package com.asset.management.controller;

import com.asset.management.entity.Asset;
import com.asset.management.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class AssetController {
    @Autowired
    private AssetService assetService;

    @RequestMapping("/findByJobNumber")
    public List<Asset> findByJobNumber(String  jobNumber){
        int job = 0;
        try {
            job = Integer.parseInt(jobNumber);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return assetService.findByJobNumber(job);
    }
    @RequestMapping("/findByAssetNumber")
    public List<Asset> findByAssetNumber(@RequestParam("assetNumber")String assetNumber){
        return assetService.findByAssetNumber(assetNumber);
    }

    /**
     * 分页显示所有数据
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("/listAsset/{page}/{size}")
    public Page<Asset> listAsset(@PathVariable("page") Integer page, @PathVariable("size") Integer size){
        return assetService.listAsset(page-1, size);
    }

    /**
     * 根据状态status查询数据，查询结果分页显示
     * @param status 查询条件
     * @param page 页数
     * @param size 每页数据条数
     * @return
     */
    @RequestMapping("listByStatus")
    public Page<Asset>listByStatus(@RequestParam("status")String status,
                                   @RequestParam("page") Integer page, @RequestParam("size") Integer size){
        Page data = assetService.listByStatus(status,page-1,size);
        return data;
    }

    /**
     * 根据id删除
     * @param id
     */
    @RequestMapping("/deleteAsset")
    public void deleteAsset(@RequestParam("id")Integer id){
        assetService.deleteAsset(id);
    }

    /**
     * 批量删除
     * @param assetIds
     */
    @RequestMapping("/bathDeleteAsset")
    public void bathDeleteAsset(@RequestParam("assetIds") String assetIds){
        assetService.bathDeleteAsset(assetIds);
    }

    @RequestMapping("/addAsset")
    public List<Optional<Asset>> addAsset(@RequestParam("ids") String ids){
        List<Optional<Asset>> list = assetService.bathFindAsset(ids);
        return list;
    }
}
