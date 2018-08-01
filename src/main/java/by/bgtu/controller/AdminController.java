package by.bgtu.controller;

import by.bgtu.configuration.ChatData;
import by.bgtu.model.*;
import by.bgtu.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Controller for admin's pages
 */
@Controller
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @RequestMapping(value = "/admin/datainit", method = RequestMethod.GET)
    public ModelAndView datainit() {
        ModelAndView modelAndView = new ModelAndView();
        for (Subject subject : ChatData.getSubjects()) {
            adminService.save(subject);
        }
        modelAndView.setViewName("redirect:/admin/subjects");
        return modelAndView;
    }

    @RequestMapping(value = {"/admin", "/admin/home"}, method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/home");
        return modelAndView;
    }

    @RequestMapping(value = {"/admin/subjects"}, method = RequestMethod.GET)
    public ModelAndView subjects() {
        ModelAndView modelAndView = new ModelAndView();
        List<Subject> subjectList = adminService.getSubjects();
        modelAndView.addObject("page", "subjects");
        modelAndView.addObject("list", subjectList);
        modelAndView.setViewName("admin/home");
        return modelAndView;
    }

    @RequestMapping(value = {"/admin/answers"}, method = RequestMethod.GET)
    public ModelAndView answers() {
        ModelAndView modelAndView = new ModelAndView();
        List<Answer> answers = adminService.getAnswers();
        modelAndView.addObject("page", "answers");
        modelAndView.addObject("list", answers);
        modelAndView.setViewName("admin/home");
        return modelAndView;
    }

    @RequestMapping(value = {"/admin/parameters"}, method = RequestMethod.GET)
    public ModelAndView parameters() {
        ModelAndView modelAndView = new ModelAndView();
        List<Parameter> parameters = adminService.getParameters();
        modelAndView.addObject("page", "parameters");
        modelAndView.addObject("list", parameters);
        modelAndView.setViewName("admin/home");
        return modelAndView;
    }

    @RequestMapping(value = "admin/parameter_edit", method = RequestMethod.GET)
    public ModelAndView parameterEdit(@RequestParam(value = "id", required = false) Integer id) {
        ModelAndView modelAndView = new ModelAndView("admin/home");
        Parameter parameter = null;
        if (id != null) {
            parameter = adminService.getParameter(id);
        }
        if (parameter == null) {
            parameter = new Parameter();
        }
        modelAndView.addObject("keyword_list", adminService.getKeyWords());
        modelAndView.addObject("page", "parameter_edit");
        modelAndView.addObject("parameter", parameter);
        return modelAndView;
    }



    private Set<KeyWord> parseKeyWords(String[] splitted) {
        Set<KeyWord> keyWords = new HashSet<>();
        if (splitted.length > 1) {
            for (int i = 1; i < splitted.length; i++) {
                String[] words = splitted[i].split("[\\(,\\)]");
                String value = words[0].trim();
                KeyWord keyWord = adminService.getKeyWord(value);
                if (keyWord == null) {
                    keyWord = new KeyWord(words[0].trim());
                    if (words.length > 1) {
                        for (int y = 1; y < words.length; y++) {
                            Word word = new Word(words[y].trim());

                            keyWord.getWords().add(word);
                        }
                    }
                }
                keyWords.add(keyWord);
            }
        }
        return keyWords;
    }

    @RequestMapping(value = "admin/parameter_edit", params = "action", method = RequestMethod.POST)
    public ModelAndView parameterEdit(@RequestParam(value = "action") String action,
                                      @RequestParam(value = "id", required = false) Integer id,
                                      @Valid @ModelAttribute("parameter") Parameter parameter,
                                      BindingResult result,
                                      RedirectAttributes redirectAttributes) {
        parseParameterName(parameter);
        ModelAndView modelAndView = new ModelAndView("admin/home");
        modelAndView.addObject("keyword_list", adminService.getKeyWords());
        modelAndView.addObject("page", "parameter_edit");

        modelAndView.addObject("parameter", parameter);
        if ("save".equals(action)) {
            try {
                adminService.save(parameter);
                modelAndView.addObject("viewMessage", "Сохранено");
            } catch (Exception e) {
                modelAndView.addObject("viewMessage", e.getMessage());
            }
            return modelAndView;
        } else if ("delete".equals(action)) {
            if (id != null) {
                try {
                    adminService.deleteParameter(id);
                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("viewMessage", e.getMessage());
                }
            }
        }
        modelAndView.clear();
        modelAndView.setViewName("redirect:/admin/parameters");
        return modelAndView;
    }


    @RequestMapping(value = {"/admin/keywords"}, method = RequestMethod.GET)
    public ModelAndView keywords() {
        ModelAndView modelAndView = new ModelAndView();
        List<KeyWord> keywords = adminService.getKeyWords();
        modelAndView.addObject("page", "keywords");
        modelAndView.addObject("list", keywords);
        modelAndView.addObject("keyword", new KeyWord());
        modelAndView.setViewName("admin/home");
        return modelAndView;
    }

    @RequestMapping(value = "admin/subject_edit", method = RequestMethod.GET)
    public ModelAndView subjectEdit(@RequestParam(value = "id", required = false) Integer id) {
        ModelAndView modelAndView = new ModelAndView("admin/home");
        modelAndView.addObject("page", "subject_edit");
        Subject subject = null;
        if (id != null) {
            subject = adminService.getSubject(id);
        }
        if (subject == null) {
            subject = new Subject();
        }
        modelAndView.addObject("subjectParameterValue_list", adminService.getParametersValues());
        modelAndView.addObject("keyword_list", adminService.getKeyWords());
        modelAndView.addObject("parameter_list", adminService.getParameters());
        modelAndView.addObject("subject", subject);
        modelAndView.addObject("param_value", new SubjectParameterValue());
        return modelAndView;
    }

    private void parseSubjectName(Subject subject) {
        if (subject == null || subject.getName() == null) return;
        String[] splitted = subject.getName().split(";");
        subject.setName(splitted[0].trim());
        Set<KeyWord> keyWords = parseKeyWords(splitted);
        subject.getKeyWords().addAll(keyWords);
    }

    private void parseParameterName(Parameter parameter) {
        if (parameter == null || parameter.getName() == null) return;
        String[] splitted = parameter.getName().split(";");
        parameter.setName(splitted[0].trim());
        Set<KeyWord> keyWords = parseKeyWords(splitted);
        parameter.getKeyWords().addAll(keyWords);
    }

    @RequestMapping(value = "admin/subject_edit", params = "action", method = RequestMethod.POST)
    public ModelAndView subjectPost(@RequestParam(value = "action") String action,
                                    @RequestParam(value = "id", required = false) Integer id,
                                    @ModelAttribute("subject") Subject subject,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes) {
        parseSubjectName(subject);
        ModelAndView modelAndView = new ModelAndView("admin/home");
        modelAndView.addObject("subjectParameterValue_list", adminService.getParametersValues());
        modelAndView.addObject("keyword_list", adminService.getKeyWords());
        modelAndView.addObject("parameter_list", adminService.getParameters());
        modelAndView.addObject("page", "subject_edit");




        modelAndView.addObject("param_value", new SubjectParameterValue());
        if ("save".equals(action) || "add".equals(action)) {
            Subject subject1;
            if ("save".equals(action)) {
                subject1 = adminService.getSubject(subject.getId());
            } else {
                subject.setId(null);
                subject1 = adminService.getSubject(subject.getName());
            }
            if (subject1 != null) {
                subject.setSubjectParameterValues(subject1.getSubjectParameterValues());
                for (SubjectParameterValue value : subject.getSubjectParameterValues()) {
                    value.setSubject(subject);
                }
            }
            try {
                subject = adminService.save(subject);
                modelAndView.addObject("viewMessage", "Сохранено");
            } catch (Exception e) {
                modelAndView.addObject("viewMessage", e.getMessage());
            }
            modelAndView.addObject("subject", subject);
            return modelAndView;
        } else if ("delete".equals(action)) {
            if (id != null) {
                try {
                    adminService.deleteSubject(id);
                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("viewMessage", e.getMessage());
                }
            }
        }
        modelAndView.clear();
        modelAndView.setViewName("redirect:/admin/subjects");
        return modelAndView;
    }


    @RequestMapping(value = "admin/param_edit", params = "action", method = RequestMethod.POST)
    public ModelAndView paramValuePost(@RequestParam(value = "action") String action,
                                       @ModelAttribute("param_value") SubjectParameterValue param_value,
                                       BindingResult result,
                                       RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView("admin/home");
        Subject subject = new Subject();
        modelAndView.addObject("subjectParameterValue_list", adminService.getParametersValues());
        modelAndView.addObject("keyword_list", adminService.getKeyWords());
        modelAndView.addObject("parameter_list", adminService.getParameters());
        modelAndView.addObject("param_value", param_value);
        if (action != null) {
            if (action.startsWith("add")) {
                subject = getSubjectAndSetRedirect(action, modelAndView);
                param_value.setId(null);
                param_value.setSubject(subject);
                subject.getSubjectParameterValues().add(param_value);
                adminService.save(subject);
            } else if (action.startsWith("save")) {
                subject = getSubjectAndSetRedirect(action, modelAndView);
                SubjectParameterValue value = subject.getParameterValue(param_value.getId());
                if (value != null) {
                    value.setParameter(param_value.getParameter());
                    value.setValue(param_value.getValue());
                } else {
                    param_value.setSubject(subject);
                    subject.getSubjectParameterValues().add(param_value);
                }
                adminService.save(subject);
            } else if (action.startsWith("delete")) {
                subject = getSubjectAndSetRedirect(action, modelAndView);
                if (param_value.getId() != null) {
                    try {
                        adminService.deleteSubjectParameterValue(param_value.getId());
                    } catch (Exception e) {
                        redirectAttributes.addFlashAttribute("viewMessage", e.getMessage());
                    }
                }
            }
        }
        modelAndView.addObject("subject", subject);
        return modelAndView;
    }

    private Subject getSubjectAndSetRedirect(@RequestParam(value = "action") String action, ModelAndView modelAndView) {
        Integer subjectId = Integer.valueOf(action.split("/")[1]);
        Subject subject1 = adminService.getSubject(subjectId);
        modelAndView.setViewName("redirect:/admin/subject_edit?action=edit&id=" + subject1.getId());
        return subject1;
    }

    @RequestMapping(value = "admin/answer_edit", method = RequestMethod.GET)
    public ModelAndView answerEdit(@RequestParam(value = "id", required = false) Integer id) {
        ModelAndView modelAndView = new ModelAndView("admin/home");
        Answer answer = null;
        if (id != null) {
            answer = adminService.getAnswer(id);
        }
        if (answer == null) {
            answer = new Answer();
        }
        modelAndView.addObject("keyword_list", adminService.getKeyWords());
        modelAndView.addObject("page", "answer_edit");
        modelAndView.addObject("answer", answer);
        return modelAndView;
    }

    private void parseAnswerValue(Answer answer) {
        if (answer == null || answer.getValue() == null) return;
        String[] splitted = answer.getValue().split(";");
        answer.setValue(splitted[0].trim());
        Set<KeyWord> keyWords = parseKeyWords(splitted);
        answer.getKeyWords().addAll(keyWords);
    }


    @RequestMapping(value = "admin/answer_edit", params = "action", method = RequestMethod.POST)
    public ModelAndView answerPost(@RequestParam(value = "action") String action,
                                   @RequestParam(value = "id", required = false) Integer id,
                                   @Valid @ModelAttribute("answer") Answer answer,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes) {
        parseAnswerValue(answer);
        ModelAndView modelAndView = new ModelAndView("admin/home");
        modelAndView.addObject("keyword_list", adminService.getKeyWords());
        modelAndView.addObject("page", "answer_edit");
        modelAndView.addObject("answer", answer);
        if ("save".equals(action)) {
            try {
                adminService.save(answer);
                modelAndView.addObject("viewMessage", "Сохранено");
            } catch (Exception e) {
                modelAndView.addObject("viewMessage", e.getMessage());
            }
            return modelAndView;
        } else if ("delete".equals(action)) {
            if (id != null) {
                try {
                    adminService.deleteAnswer(id);
                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("viewMessage", e.getMessage());
                }
            }
        }
        modelAndView.clear();
        modelAndView.setViewName("redirect:/admin/answers");
        return modelAndView;
    }

    @RequestMapping(value = "admin/keyword_edit", params = "action", method = RequestMethod.POST)
    public ModelAndView keywordPost(@RequestParam(value = "action") String action,
                                    @ModelAttribute("keyword") KeyWord keyWord,
                                    RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView("admin/home");
        modelAndView.addObject("keyword_list", adminService.getKeyWords());
        modelAndView.addObject("page", "keyword_edit");
        modelAndView.addObject("keyword", keyWord);
        if ("save".equals(action)) {
            try {
                adminService.save(keyWord);
                redirectAttributes.addFlashAttribute("viewMessage", "Сохранено");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("viewMessage", e.getMessage());
            }
        } else if ("delete".equals(action)) {
            if (keyWord != null) {
                try {
                    adminService.deleteKeyWord(keyWord.getId());
                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("viewMessage", e.getMessage());
                }
            }
        }
        modelAndView.setViewName("redirect:/admin/keywords");
        return modelAndView;
    }
}
