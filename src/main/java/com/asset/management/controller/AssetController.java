package com.asset.management.controller;

import com.asset.management.annotation.LoginToken;
import com.asset.management.annotation.PassToken;
import com.asset.management.entity.Asset;
import com.asset.management.entity.ResultSet;
import com.asset.management.service.AssetService;
import com.asset.management.utils.HttpsUtils;
import com.asset.management.utils.JsonUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*",maxAge = 3600)
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
    @LoginToken
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
    @PassToken
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
    @LoginToken
    public ResultSet addAsset(@RequestParam("ids") String ids) throws IOException {
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
        String tokenToOa = DigestUtils.md5DigestAsHex(str.getBytes());
        resultSet.setTokenToOa(tokenToOa);

        String oaUrl = "http://eicommon.37wan.com/api.php/taker/rouseInterface";
        Map<String,Object> param = new HashMap<>();

        Map<String,Object> oaData = new HashMap<>();
        oaData.put("type","it_manager");
        oaData.put("info",collect);
        String oaJson = JsonUtils.deserializer(oaData);

        param.put("data",oaData);
        param.put("appId",appId);
        param.put("nonce","zdq888ji");
        param.put("timestamp",time);
        param.put("interfaceId",interfaceId);
        param.put("token",tokenToOa);
        String json = JsonUtils.deserializer(param);

        String result = HttpsUtils.doPost(oaUrl,json);
        //接口返回的数据转化为前端响应体
        ResultSet resultData = JsonUtils.serializable(result,ResultSet.class);
        resultSet.setCode(resultData.getCode());
        resultSet.setMsg(resultData.getMsg());

        return resultSet;
    }

    @RequestMapping("/updateAsset")
    public ResultSet updateAsset(@RequestBody Asset asset){
        ResultSet resultSet = new ResultSet();
        Asset result = assetService.updateAsset(asset);
        if(result != null){
            resultSet.setSuccess(true);
        }else {
            resultSet.setSuccess(false);
        }
        return resultSet;
    }

    @RequestMapping("/updateAssetStatus")
    public void updateAssetStatus(@RequestParam("ids") String ids){
        assetService.updateAssetStatus(ids);
    }
}
