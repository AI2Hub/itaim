package com.asset.management.controller;

import com.asset.management.entity.Asset;
import com.asset.management.entity.ResultSet;
import com.asset.management.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

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

    /**
     * 插入数据到OA系统
     * @param ids 选中数据的id
     * @return
     */
    @RequestMapping("/addAsset")
    public ResultSet addAsset(@RequestParam("ids") String ids){
        //根据id查询资产信息
        List<Asset> data = assetService.bathFindAsset(ids);

//        List<Integer> ret =  data.stream().map(Asset::getJobNumber).collect(Collectors.toList());
        //从查询结果中筛选资产编号和工号
        Map<String,Integer> collect = data.stream().collect(
                Collectors.toMap(
                        t -> t.getAssetNumber().trim(),
                        Asset::getJobNumber
                ));

        //讲资产编号和工号数据集合添加到返回数据resultSet
        ResultSet resultSet = new ResultSet();
        resultSet.setData(collect);

        //提交到OA接口的参数（token组成字符串）
        String appId =  "qSymvYkZ4a2caQNVgKHG";
        String appSecret = "XchRcjaVQySxS8G2Vzf3CZamY7zxVWgJ";
        String interfaceId = "99f2ac375978e374557067455b855eab";
        //生成8位随机数
//        String random = ((Math.random()*9+1)*10000000)+"";
//        resultSet.setRandom(random);
        //获取当前时间戳
        long time = new Date().getTime();
        resultSet.setTime(time);
        String str = appId+appSecret+interfaceId+"zdq888ji"+time;
        //MD5加密生成提交到OA的token
        String token = DigestUtils.md5DigestAsHex(str.getBytes());
        resultSet.setToken(token);

        return resultSet;
    }

    @RequestMapping("/updateAsset")
    public void updateAsset(@RequestParam("ids") String ids){
        assetService.updateAsset(ids);
    }
}
