@Entity
@Table(name = "transcript_data")
@Data
public class TranscriptData {

    @Id
    private Long id;

    @Column(name = "transcript_id")
    private Long transcriptId;

    @Column(name = "program")
    private String program;
}
