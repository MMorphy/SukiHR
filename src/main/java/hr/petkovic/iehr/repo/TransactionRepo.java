package hr.petkovic.iehr.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hr.petkovic.iehr.DTO.SiteWithTotalDebtDTO;
import hr.petkovic.iehr.DTO.UserWithTotalDebtDTO;
import hr.petkovic.iehr.DTO.report.ReportingBaseDTO;
import hr.petkovic.iehr.DTO.report.SiteMonthReportDTO;
import hr.petkovic.iehr.DTO.report.SiteTotalReportDTO;
import hr.petkovic.iehr.DTO.report.SiteYearReportDTO;
import hr.petkovic.iehr.DTO.report.TransactionMonthReportDTO;
import hr.petkovic.iehr.DTO.report.TransactionTotalReportDTO;
import hr.petkovic.iehr.DTO.report.TransactionYearReportDTO;
import hr.petkovic.iehr.DTO.report.UserMonthReportDTO;
import hr.petkovic.iehr.DTO.report.UserTotalReportDTO;
import hr.petkovic.iehr.DTO.report.UserYearReportDTO;
import hr.petkovic.iehr.entity.Transaction;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {

	public List<Transaction> findAllByCreatedBy_Username(String username);

	public List<Transaction> findAllByCreatedBy_UsernameAndType_SubType(String username, String subType);

	public List<Transaction> findAllByCreatedBy_Roles_NameInOrType_SubType(List<String> roleNames, String subtype);

	public List<Transaction> findAllByType_MainType(String mainType);

	public List<Transaction> findAllByType_SubType(String subtype);

	@Query("SELECT t FROM Transaction t WHERE t.amount <> 0")
	public List<Transaction> findAllWithValues();

	@Query("SELECT t FROM Transaction t WHERE t.amount <> 0 AND t.createdBy.username = ?1")
	public List<Transaction> findAllWithValuesForUser(String username);

	@Query("SELECT new hr.petkovic.iehr.DTO.SiteWithTotalDebtDTO(t.site, SUM(t.debt.amount)) "
			+ "FROM Transaction t WHERE t.debt.amount <> 0 GROUP BY t.site ")
	List<SiteWithTotalDebtDTO> findAllSitesAndDebt();

	@Query("SELECT new hr.petkovic.iehr.DTO.SiteWithTotalDebtDTO(t.site, SUM(t.debt.amount)) "
			+ "FROM Transaction t WHERE t.createdBy.username = :username AND t.debt.amount <> 0 GROUP BY t.site")
	List<SiteWithTotalDebtDTO> findAllSitesAndDebtCreatedBy(@Param("username") String username);

	@Query("SELECT new hr.petkovic.iehr.DTO.UserWithTotalDebtDTO(t.createdBy, SUM(t.debt.amount)) "
			+ "FROM Transaction t GROUP BY t.createdBy")
	List<UserWithTotalDebtDTO> findAllDebtForUser();

	@Query("SELECT sum(t.amount) FROM Transaction t WHERE t.createdBy.username = :username AND t.type.mainType = :mainType")
	public Double findAllTransactionsOfMainTypeForUsername(@Param("username") String username,
			@Param("mainType") String mainType);

	@Query("SELECT sum(t.amount) FROM Transaction t WHERE t.createdBy.username = :username AND t.type.subType = :subType")
	public Double findAllTransactionsOfSubTypeForUsername(@Param("username") String username,
			@Param("subType") String subType);

	@Query("SELECT sum(t.amount) FROM Transaction t WHERE t.createdBy.username = :username AND t.type.subType IN (:subtypes)")
	public Double findSumOfAllTransactionsForUserOfSubtypes(@Param("username") String username,
			@Param("subtypes") List<String> subtypes);

	public Transaction findByDebtId(Long id);

	@Query("SELECT sum(t.amount) FROM Transaction t WHERE t.type.subType = :subType")
	public Double findSumOfAllTransactionsOfSubtype(@Param("subType") String subType);

	@Query("SELECT sum(t.amount) FROM Transaction t WHERE t.type.subType IN (:subtypes)")
	public Double findSumOfTransactionsWithSubtypes(@Param("subtypes") List<String> subtypes);

	@Query("SELECT t FROM Transaction t WHERE t.createdBy.username = :username AND t.type.subType IN (:subtypes)")
	public List<Transaction> findAllTransactionsForUserWithSubtype(@Param("username") String username,
			@Param("subtypes") List<String> subtypes);

	@Query("SELECT new hr.petkovic.iehr.DTO.report.UserTotalReportDTO(t.type.subType, sum(t.amount), t.createdBy.username) FROM Transaction t where t.createdBy.username = :username GROUP BY t.createdBy.username, t.type.subType")
	public List<UserTotalReportDTO> findUserTotalReport(String username);

	@Query("SELECT new hr.petkovic.iehr.DTO.report.ReportingBaseDTO(t.type.subType, sum(t.amount)) FROM Transaction t where t.type.mainType = :type GROUP BY t.type.subType")
	public List<ReportingBaseDTO> findTotalExpensesReport(String type);

	@Query("SELECT new hr.petkovic.iehr.DTO.report.UserYearReportDTO(t.type.subType, sum(t.amount), t.createdBy.username, year(t.createDate)) FROM Transaction t where t.createdBy.username = :username GROUP BY t.createdBy.username, t.type.subType, year(t.createDate)")
	public List<UserYearReportDTO> findUserYearReport(String username);

	@Query("SELECT new hr.petkovic.iehr.DTO.report.UserMonthReportDTO(t.type.subType, sum(t.amount), t.createdBy.username, year(t.createDate), month(t.createDate)) FROM Transaction t where t.createdBy.username = :username GROUP BY t.createdBy.username, t.type.subType, year(t.createDate), month(t.createDate)")
	public List<UserMonthReportDTO> findUserMonthReport(String username);

	@Query("SELECT new hr.petkovic.iehr.DTO.report.SiteTotalReportDTO(t.type.subType, sum(t.amount), t.site.name, t.site.address) FROM Transaction t GROUP BY t.site.name, t.type.subType, t.site.address")
	public List<SiteTotalReportDTO> findSiteTotalReport();

	@Query("SELECT new hr.petkovic.iehr.DTO.report.SiteYearReportDTO(t.type.subType, sum(t.amount), t.site.name, t.site.address, year(t.createDate)) FROM Transaction t GROUP BY t.site.name, t.type.subType, t.site.address,year(t.createDate)")
	public List<SiteYearReportDTO> findSiteYearReport();

	@Query("SELECT new hr.petkovic.iehr.DTO.report.SiteMonthReportDTO(t.type.subType, sum(t.amount), t.site.name, t.site.address, year(t.createDate), month(t.createDate)) FROM Transaction t GROUP BY t.site.name, t.type.subType, t.site.address,year(t.createDate), month(t.createDate)")
	public List<SiteMonthReportDTO> findSiteMonthReport();

	@Query("SELECT new hr.petkovic.iehr.DTO.report.TransactionTotalReportDTO(t.type.subType, sum(t.amount)) FROM Transaction t GROUP BY t.type.subType")
	public List<TransactionTotalReportDTO> findTransactionTotalReport();

	@Query("SELECT new hr.petkovic.iehr.DTO.report.TransactionYearReportDTO(t.type.subType, sum(t.amount), year(t.createDate)) FROM Transaction t GROUP BY t.type.subType, year(t.createDate)")
	public List<TransactionYearReportDTO> findTransactionYearReport();

	@Query("SELECT new hr.petkovic.iehr.DTO.report.TransactionMonthReportDTO(t.type.subType, sum(t.amount), year(t.createDate), month(t.createDate)) FROM Transaction t GROUP BY t.type.subType, year(t.createDate), month(t.createDate)")
	public List<TransactionMonthReportDTO> findTransactionMonthReport();

	@Query("SELECT t FROM Transaction t WHERE t.type.subType = :subType AND t.createdBy.username = :username AND t.createDate >= :firstDate")
	public List<Transaction> findAllPayTransactionForUser(@Param("firstDate") Date firstDate,
			@Param("username") String username, @Param("subType") String subType);

}
