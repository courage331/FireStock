package kr.co.firestock.repository;

import kr.co.firestock.vo.CryptoCurrencyVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CryptoCurrencyRepository extends JpaRepository<CryptoCurrencyVO, String> {

    CryptoCurrencyVO findByMarket(String market);

    @Query(value = "select * from cryptocurrency c where c.market in :markets", nativeQuery = true)
    List<CryptoCurrencyVO> findMarketIn(String[] markets);
}
