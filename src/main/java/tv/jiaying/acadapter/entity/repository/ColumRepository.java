package tv.jiaying.acadapter.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tv.jiaying.acadapter.entity.Colum;

import java.util.List;

public interface ColumRepository extends JpaRepository<Colum,Long> {

        List<Colum> findAllByProviderId(String providerId);

}
