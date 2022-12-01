package com.coc.es;

import com.coc.es.service.EsJobService;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1NamespaceList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.credentials.AccessTokenAuthentication;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@SpringBootTest
class CoCEsApplicationTests {

    @Resource
    private EsJobService esJobService;

//    @Test
//    public void test1(){
//        NativeSearchQuery query = new NativeSearchQueryBuilder()
//                .withQuery(QueryBuilders.boolQuery()
//                        .must(QueryBuilders.multiMatchQuery("小黑子", "job_name", "job_desc", "select_industry", "job_keyword"))
//                        .filter(QueryBuilders.matchQuery("deleted", 0)))
//                .build();
//        SearchHits<Job> search = template.search(query, Job.class);
//        List<Job> jobs = search.stream().map(SearchHit::getContent).collect(Collectors.toList());
//        search.forEach(s -> {
//            System.out.println(s.getContent());
//        });
//    }

    @Test
    public void test2() {
        esJobService.SynchronizationJobToEs();
    }

    @SneakyThrows
    @Test
    public void test3() {
        // 1).获取连接客户端方式一（jvm环境变量传入kubernetes的master节点的url与port，若该应用是跑在k8s上的会自动传入）
        ApiClient client = new ClientBuilder()
                .setBasePath("https://47.110.49.175:6443")
                .setVerifyingSsl(false)
                .setAuthentication(new AccessTokenAuthentication("eyJhbGciOiJSUzI1NiIsImtpZCI6IkFMM0ptQVlab19lbGZOM1g5SFdyMjZkZUhTYS1fVFZsSWpCdERMRmhuREUifQ.eyJhdWQiOlsiaHR0cHM6Ly9rdWJlcm5ldGVzLmRlZmF1bHQuc3ZjLmNsdXN0ZXIubG9jYWwiXSwiZXhwIjoxNjY2MDg4OTcyLCJpYXQiOjE2NjYwODUzNzIsImlzcyI6Imh0dHBzOi8va3ViZXJuZXRlcy5kZWZhdWx0LnN2Yy5jbHVzdGVyLmxvY2FsIiwia3ViZXJuZXRlcy5pbyI6eyJuYW1lc3BhY2UiOiJrdWJlLXN5c3RlbSIsInNlcnZpY2VhY2NvdW50Ijp7Im5hbWUiOiJhcGktYWRtaW4iLCJ1aWQiOiI5NjBjNzg5NC0zNmZkLTQ4M2MtODY2Mi04MzkzYmY4Y2RlOTQifX0sIm5iZiI6MTY2NjA4NTM3Miwic3ViIjoic3lzdGVtOnNlcnZpY2VhY2NvdW50Omt1YmUtc3lzdGVtOmFwaS1hZG1pbiJ9.oUWbxGcJvu2OpYC7UhUsyOa_vQauol1_igjWyccpFCuQv6RiGHzQcLE63-4oPTrN08FWDPMtcLBGNswrEPr78o30skyhBUSDQDes3bqjVaIkZ5bNuEOTqjWkxJr5epGFBNIWqMDeoalo_93pmt1pU3B5wf199reOtwUSO6L3fA7H_6w6jnR7sq5LTW1yJmVnfygmaKPsKtjNas-iMnDto4jg6Prnm73xXrn71JCSsRIRgwLB-_tqTYmgeD0AINiNgbWMLEAbxcwXfQ7t4CdEBxP2_9ARvYjnpbUVoMYEv5A85EGYEfnoVQT78GIV7IYvF6DLTG3oXvvpPWXI4iV53w"))
                .build();
        // 2.)获取连接客户端方式二（加载配置文件连接k8s，configFilePath为配置文件地址，配置文件可以到rancher上获取）
        // ApiClient client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader("configFilePath"))).build();

        // 将客户端传入全局配置
        Configuration.setDefaultApiClient(client);
        // 获取kubernetes的api对象
        CoreV1Api api = new CoreV1Api();
        V1NamespaceList namespace = api.listNamespace("true", null, null, null, null, null, null, null, null, null);
        namespace.getItems().forEach(s -> {
            System.out.println(s.getMetadata().getName());
        });
//        V1Status status = api.deleteNamespace("haifeng-space", "true", null, null, null, null, null);
//        System.out.println(status);
    }

    @Test
    public void test4() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://110.40.214.91:3306/CoC?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8";
            String user = "root";
            String password = "haifeng328";
            connection = DriverManager.getConnection(url, user, password);
            String sql = "drop database huao1";
            statement = connection.prepareStatement(sql);
            int i = statement.executeUpdate();
            System.out.println(i);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            statement.close();
            connection.close();
        }
    }

    @Test
    public void test5() {
        int i = kthGrammar(30, 21);
        System.out.println(i);

    }

    /**
     * 空间占用率太高
     */
    public int kthGrammar(int n, int k) {
        if (n == 1 || k == 1) {
            return 0;
        }
        if (n == 2 && k == 2) {
            return 1;
        }
        Queue<Integer> then = new ArrayDeque<Integer>();
        List<Integer> next = new ArrayList<>();
        then.offer(0);
        then.offer(1);
        for(int i = 2; i <= n; ++i) {
            next.clear();
            next.addAll(then);
            next.addAll(NOTQueue(then));
            then.clear();
            then.addAll(next);
        }
        return next.get(k - 1);
    }

    // 对队列的每位进行取反
    public Queue<Integer> NOTQueue(Queue<Integer> queue) {
        Queue<Integer> res = new ArrayDeque<Integer>();
        while (!queue.isEmpty()) {
            res.offer(queue.poll() ^ 1);
        }
        return res;
    }
}
