package controller;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping(value="/test")
public class TestRestController {
	public TestRestController() {
		System.out.println("--------------------------------------");
	}
    
    //接收get请求
    @RequestMapping(value="/test",method=RequestMethod.GET)
    //可以自动根据名称映射参数，也可以手动指定
    public String test(@RequestParam(value="name",defaultValue="admin")String username,HttpServletRequest request){
    //public String test(@RequestParam("name")String username,HttpServletRequest request){
    //public String test(String name,HttpServletRequest request){
        //获取到spring的applicationContext对象
        
        System.out.println("-------------------> name:" +username);
        return "test";
    }
    
    //redirect/forward:url方式转到另一个Action进行连续的处理
    @RequestMapping("/redirect")
    public String testRedirect(){
        return "redirect:/index.jsp";
    }
    
    //上传1
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(@RequestParam("name") String name,
            @RequestParam("file") MultipartFile file){
        System.out.println("param:"+name);
        if(!file.isEmpty()){
            System.out.println("upload ok");
        }
        return "redirect:/login.jsp";
    }
    
    //接收ajax请求
    //使用url的表达式动态传递参数
    //返回json
    @RequestMapping("/uri/{userId}")
    //多个url参数可以用多个注解和方法参数来接收
    public @ResponseBody Map<String,String> uriTemplate(@PathVariable String userId){
    //也可以这么写
    //public @ResponseBody Map uriTemplate(@PathVariable("userId") String loginUserId){
        System.out.println("url的参数为:"+userId);
        Map<String, String> map = new HashMap<String, String>();
        map.put("user", userId);
        return map;
    }
    
    //测试另外一种json方式
    @RequestMapping(value="/nextjson")
    //写在方法上还是返回参数前效果一致
    //@ResponseBody
    @ResponseBody
    public  Object testNextJson(){
        return new Object(){
        };
    }
    
    //调用业务类执行保存操作
    @RequestMapping(value="/save",produces="application/json")
    public @ResponseBody Map<String, String> testSave(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("isOk", "success");
        return map;
    }
}