package by.bgtu.controller;

import by.bgtu.model.Conversation;
import by.bgtu.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;

/**
 * Controller for chat page
 */
@Controller
public class ChatController {

    private final ChatService chatService;

    private final Conversation conversation = new Conversation();

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
    public ModelAndView homeGet() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("conversation", conversation);
        modelAndView.addObject("question", "");
        modelAndView.setViewName("home");
        return modelAndView;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String homePost(ModelMap map,
                           @ModelAttribute(value = "question") String question) {
        String answer = chatService.getAnswers(question);
        conversation.add(question, answer);
        map.addAttribute("conversation", conversation);
        map.addAttribute("question", "");
        return "home";
    }

    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public ModelAndView about() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("about");
        return modelAndView;
    }

    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public ModelAndView error403() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/error/403");
        return modelAndView;
    }

}
