package io.vzhidu.chassis.common.spring.feign

class HttpbinResponse {

    /**
     * 请求传递的查询参数
     */
    Map<String, Object> args

    /**
     * 请求时传递的数据，字符串格式
     */
    String data

    /**
     * 请求时传递的文件
     */
    Object files

    /**
     * 请求时传递的form表单参数
     */
    Object form

    /**
     * 请求headers
     */
    Map<String, String> headers

    /**
     * 请求的数据，json格式
     */
    Object json

    /**
     * 客户端IP
     */
    String origin

    /**
     * 请求的资源地址
     */
    String url
}
