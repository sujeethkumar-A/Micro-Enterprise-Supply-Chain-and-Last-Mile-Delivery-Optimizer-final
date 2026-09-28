package com.example.supplychain.repository;

import com.example.supplychain.model.ArtisanCluster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtisanClusterRepository extends JpaRepository<ArtisanCluster, Long> {
}
