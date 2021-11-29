package kr.co.firestock.repository;

import kr.co.firestock.vo.CryptoCurrencyVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoCurrencyRepository extends JpaRepository<CryptoCurrencyVO, String> {
}
