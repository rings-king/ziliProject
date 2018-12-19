package com.jux.mtqiushui.resources.repository;

import com.jux.mtqiushui.resources.model.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {
}
