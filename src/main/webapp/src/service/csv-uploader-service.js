import {api} from './api';

/**
 * Data access layer to upload csv
 * @param file
 * @param onUploadProgress
 * @returns {Promise<AxiosResponse<any>>}
 * @author Sihyun Park
 */
function CsvUploaderService(file, onUploadProgress) {
    let data = new FormData();
    data.append("file", file);

    return api.post("/csv/upload", data, {
        headers: {
            "Content-Type": "multipart/form-data",
        },
        onUploadProgress
    });
}

export default CsvUploaderService;