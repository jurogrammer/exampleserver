package juro.exampleserver.repository.bookmark;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
	List<Bookmark> findByUserId(Long userId);

	Optional<Bookmark> findByUserIdAndProductId(Long userId, Long productId);
}
