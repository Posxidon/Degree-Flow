import React, { useEffect, useState } from 'react';
import UnitTracker from './UnitTracker';
import './UnitTracker.css';

function UnitTrackerSection() {
  const [requiredProgress, setRequiredProgress] = useState(0);
  const [technicalProgress, setTechnicalProgress] = useState(0);
  const [electiveProgress, setElectiveProgress] = useState(0);
  const [overallProgress, setOverallProgress] = useState(0);


  const [requiredTotal, setRequiredTotal] = useState(0);
  const [technicalTotal, setTechnicalTotal] = useState(0);
  const [electiveTotal, setElectiveTotal] = useState(0);
  const [overallTotal, setOverallTotal] = useState(0);

  const [userId, setUserId] = useState(null);
  const [transcriptId, setTranscriptId] = useState(null);
  const [requirementsExist, setRequirementsExist] = useState(null);

  useEffect(() => {
    const storedUserId = localStorage.getItem('userId');
    if (!storedUserId) {
      console.warn("User ID not found in localStorage.");
      return;
    }

    setUserId(storedUserId);

    // Step 1: Get transcriptId
    fetch(`http://localhost:8080/api/transcript/getId?userId=${storedUserId}`)
      .then(res => res.json())
      .then(data => {
        const tid = data.transcript_id;
        setTranscriptId(tid);
        console.log("Transcript ID:", tid);

        // Step 2: Check if course requirements exist
        return fetch(`http://localhost:8080/api/transcript/requirementExists?transcriptId=${tid}`);
      })
      .then(res => res.json())
      .then(exists => {
        setRequirementsExist(exists);
      })
      .catch(err => {
        console.error("Error fetching transcript or requirements:", err);
      });
  }, []);

  useEffect(() => {
    if (requirementsExist && transcriptId) {
      // Step 3: Fetch requirement groups
      fetch(`http://localhost:8080/api/requirementGroups?transcriptId=${transcriptId}`)
        .then(res => res.json())
        .then(groups => {
          let required = 0;
          let technical = 0;
          let elective = 0;
          let overall =0;

          let comRequired = 0;
          let comTechnical = 0;
          let comElective = 0;
          let comOverall =0;

          groups.forEach(group => {
            const numCom = group.numCompleted;
            const numReq = group.numRequired;
            switch (group.type) {
              case "Required":
                comRequired += numCom;
                comOverall += numCom;

                required += numReq;
                overall += numReq;
                break;
              case "Technical Electives":
                comTechnical += numCom;
                comOverall += numCom;

                technical += numReq;
                overall += numReq;


                break;
              case "Electives":
                comElective += numCom;
                comOverall += numCom;

                elective += numReq;
                overall += numReq;
                break;
              default:
                break;
            }
          });

          setRequiredProgress(comRequired);
          setTechnicalProgress(comTechnical);
          setElectiveProgress(comElective);
          setOverallProgress(comOverall);

          setRequiredTotal(required);
          setTechnicalTotal(technical);
          setElectiveTotal(elective);
          setOverallTotal(overall);
        })
        .catch(err => console.error("Error fetching requirement groups:", err));
    }
  }, [requirementsExist, transcriptId]);

  return (
    <div className="unit-tracker-container">
      <h3 className="unit-tracker-title">Overall Progress</h3>
      <UnitTracker label="Overall Degree Progress" progress={overallProgress} total={overallTotal} />
      <UnitTracker label="Required Courses" progress={requiredProgress} total={requiredTotal} />
      <UnitTracker label="Technical Courses" progress={technicalProgress} total={technicalTotal} />
      <UnitTracker label="Elective Courses" progress={electiveProgress} total={electiveTotal} />
    </div>
  );
}

export default UnitTrackerSection;
