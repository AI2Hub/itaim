package com.asset.management.dao;

import com.asset.management.entity.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetDao extends JpaRepository<Asset,Integer>, JpaSpecificationExecutor<Asset> {
    Page<Asset> findByStatus(String status, Pageable pageable);

    @Query(value="select * from AIM_assetsrelatedstatement where JobNumber = ?1",nativeQuery = true)
    List<Asset> findByJobNumber(int jobNumber);

    @Query(value="select * from AIM_assetsrelatedstatement where AssetNumber = ?1",nativeQuery = true)
    List<Asset> findByAssetNumber(String assetNumber);

    @Query(value="select * from AIM_assetsrelatedstatement where ID = ?1",nativeQuery = true)
    Asset findById(int id);


}
