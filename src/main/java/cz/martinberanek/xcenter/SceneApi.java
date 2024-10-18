package cz.martinberanek.xcenter;

import cz.martinberanek.xcenter.xcenter.XCenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scene/")
@RequiredArgsConstructor
public class SceneApi {

    private final XCenterService xCenterService;

    @GetMapping("{sceneId}")
    public Boolean isEnabled(@PathVariable String sceneId) {
        return xCenterService.getSceneEnabled(sceneId);
    }

    @PostMapping("{sceneId}/enable")
    public void enable(@PathVariable String sceneId) {
        xCenterService.setSceneEnabled(sceneId, true);
    }

    @PostMapping("{sceneId}/disable")
    public void disable(@PathVariable String sceneId) {
        xCenterService.setSceneEnabled(sceneId, false);
    }

}
