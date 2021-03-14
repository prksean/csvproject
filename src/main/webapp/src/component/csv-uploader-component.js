import React, {useState, useCallback} from 'react';
import {useDropzone} from 'react-dropzone';
import {CProgress, CJumbotron} from '@coreui/react';
import CsvUploaderService from "../service/csv-uploader-service";

import '../App.css';

/**
 * CsvUploader component
 * @author Sihyun Park
 */
function CsvUploader() {

    const [fileState, setFileState] = useState({
        progress: 0,
        uploadedFile: undefined,
        message: "",
        cnt: 0,
        time: 0
    });

    const onDrop = useCallback(acceptedFiles => {
        if (acceptedFiles[0].type === "text/csv") {
            setFileState({
                progress: 0,
                uploadedFile: acceptedFiles[0],
                message: "",
                cnt: 0,
                time: 0
            });

            CsvUploaderService(acceptedFiles[0], (event) => {
                setFileState({
                    progress: Math.round((100 * event.loaded) / event.total)
                });
            })
                .then((response) => {
                    setFileState({
                        progress: 0,
                        message: response.data.message,
                        cnt: response.data.cnt,
                        time: response.data.time
                    });
                })
                .catch(() => {
                    setFileState({
                        progress: 0,
                        uploadedFile: undefined,
                        message: "File upload failed!",
                        cnt: 0,
                        time: 0
                    });
                })
        } else {
            setFileState({
                progress: 0,
                uploadedFile: acceptedFiles[0],
                message: "You can only upload CSV file.",
                cnt: 0,
                time: 0
            });
        }
    }, [])

    const {getRootProps, getInputProps} = useDropzone({onDrop});

    return (
        <CJumbotron className="Custom-yellow-bg">
            <h1 className="Custom-black">WELCOME TO CSV FILE UPLOADER!!!</h1>
            {fileState.progress === 0 &&
            <div data-testid="dropzone" {...getRootProps()} className="Talk-bubble">
                <input disabled={fileState.progress > 0} data-testid="drop-input" {...getInputProps()} />
                <p>Drag 'n' drop csv file here, or click to select file</p>
            </div>
            }
            <p className="Custom-black">{fileState.message}</p>
            {fileState.progress > 0 &&
            <div>
                <CProgress animated striped showValue color="info" value={fileState.progress}
                           className="mb-1 Custom-yellow-bg"/>
            </div>
            }
            {fileState.progress === 100 &&
            <p className="lead Custom-black">File upload completed. File is now being saved in the database. Please wait.
            </p>
            }
            {fileState.cnt > 0 &&
            <p className="Custom-black">Process time to
                insert <b>{fileState.cnt.toLocaleString()}</b> records: <strong>{fileState.time}</strong> seconds.
            </p>
            }
        </CJumbotron>
    );
}

export default CsvUploader;

