@RestController
@RequestMapping("/api/transcript")
public class TranscriptLookupController {

    private final TranscriptLookupService lookupService;

    public TranscriptController(TranscriptLookupService lookupService) {
        this.lookupService = lookupService;
    }

    @GetMapping("/program")
    public ResponseEntity<String> getProgram(@RequestParam String transcriptId) {
        String program = lookupService.findProgramByTranscriptId(transcriptId);
        return ResponseEntity.ok(program);
    }
}
