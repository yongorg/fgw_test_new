package cn.com.wavetop;

import cn.com.wavetop.DBConn.OracleConnPool;
import cn.com.wavetop.DBConn.wb.*;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;




/**
 * @Author yongz
 * @Date 2019/8/12、14:26
 */
public class Main {

    private static JdbcTemplate anqzj = new JdbcTemplate(AnqzjConnPool.getDataSources()); // 安监局
    private static JdbcTemplate gtqzj = new JdbcTemplate(GtqzjConnPool.getDataSources()); // 规土局
    private static JdbcTemplate jtwqzj = new JdbcTemplate(JtwqzjConnPool.getDataSources());//交通局
    private static JdbcTemplate jwqzj = new JdbcTemplate(JwqzjConnPool.getDataSources());//建委
    private static JdbcTemplate lhqzj = new JdbcTemplate(LhqzjConnPool.getDataSources());//绿化局
    private static JdbcTemplate qxjqzj = new JdbcTemplate(QxjqzjConnPool.getDataSources());//气象局
    private static JdbcTemplate swqzj = new JdbcTemplate(SwqzjConnPool.getDataSources());//水务局
    private static JdbcTemplate testqzj = new JdbcTemplate(TestqzjConnPool.getDataSources());//
    private static JdbcTemplate wgqzj = new JdbcTemplate(WgqzjConnPool.getDataSources());//文广局
    private static JdbcTemplate wjwqzj = new JdbcTemplate(WjwqzjConnPool.getDataSources());//卫计委

    private static JdbcTemplate oraHandle = new JdbcTemplate(OracleConnPool.getDataSources());//卫计委

    private static Logger logger = Logger.getLogger(Main.class);  // log4j日志

    public static void main(String[] args) {


        int action  = 1;
        switch (action){

            //  从各个委办抽取数据
            case 1:
                logger.info("action: "+action);
                updateWdb(oraHandle); // 同步表数据
                deal_fgwdata(oraHandle); //处理发改委数据
                deal_zjw_spxx(oraHandle);
                break;

        }

    }

    private static void deal_zjw_spxx(JdbcTemplate oraHandle) {
        logger.info("deal_zjw_spxx begin");

        oraHandle.queryForList("select max(to_char(createdate,'yyyy-mm-dd')) from TAB_CPTC_CBSJSPXX\n");
        logger.info("deal_zjw_spxx end");
    }

    // 处理发改委数据
    private static int deal_fgwdata(JdbcTemplate oraHandle) {
        logger.info("deal_fgwdata begin");

        oraHandle.update("delete from TEMP_APPROVE_ITEM_INFO where item_code like '%%FGW%%'");

        oraHandle.update("INSERT INTO TEMP_APPROVE_ITEM_INFO (SELECT r.* FROM TEMP_APPROVE_ITEM_INFO_HZ r where item_code like '%%FGW%%')");
        // 删除重复数据
        oraHandle.update("delete from TEMP_APPROVE_ITEM_INFO where (APPROVAL_ITEMID,deal_time) in (select APPROVAL_ITEMID,min(deal_time) from TEMP_APPROVE_ITEM_INFO GROUP BY APPROVAL_ITEMID HAVING COUNT(*)>1)");

        logger.info("deal_fgwdata end");
        return 0;
    }

    // 同步表数据
    private static int updateWdb(JdbcTemplate oraHandle) {
        logger.info("updateWdb begin");

        /*
        //更新表QZJ_SPXX_DOC_ATT_INFO
        oraHandle.update("insert into QZJ_SPXX_DOC_ATT_INFO (select q.*,'0' as my_flag from  gdzcweb.QZJ_SPXX_DOC_ATT_INFO q  where q.createdate>(select max(createdate) from QZJ_SPXX_DOC_ATT_INFO))");
        //更新表QZJ_SPXX_DOC_INFO
        oraHandle.update("DROP TABLE QZJ_SPXX_DOC_INFO");
        oraHandle.update("create table QZJ_SPXX_DOC_INFO as select r.*,'0' as my_flag from GDZCWEB.QZJ_SPXX_DOC_INFO r");*/
        //oraHandle.update("insert into QZJ_SPXX_DOC_INFO (select q.*,'0' as my_flag from  gdzcweb.QZJ_SPXX_DOC_INFO q  where q.createdate>(select max(createdate) from QZJ_SPXX_DOC_INFO))");

        //更新表REGION_APPLY_PROJECT_INFO
        oraHandle.update("DROP TABLE REGION_APPLY_PROJECT_INFO");

        oraHandle.update("create table REGION_APPLY_PROJECT_INFO as select r.* from REGION_APPLY_PROJECT_INFO_HZ r");

        oraHandle.update("update REGION_APPLY_PROJECT_INFO set VALIDITY_FLAG='2' WHERE PROJECT_NAME LIKE '%%大修%%' or PROJECT_NAME LIKE '%%装修%%'  or PROJECT_NAME LIKE '%%修缮%%' or project_name like '%%养护%%' or project_name like '%%维修%%'");

        oraHandle.update("update REGION_APPLY_PROJECT_INFO t set t.VALIDITY_FLAG=0 where t.pro_id in(select r.pro_id from DEAL_FGDZC_WDB r where r.pro_id is not null)");

        // 问题 1：oraHandle.update("update REGION_APPLY_PROJECT_INFO t set t.VALIDITY_FLAG=0 where t.project_code in(select r.project_code from DEAL_CX_PROJECT r)");

        logger.info("updateWdb end");
        return 0;
    }
}
