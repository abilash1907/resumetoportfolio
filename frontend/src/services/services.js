
export const BASE_URL = 'http://localhost:8080/api';

const extractContent = async (file) => {
    try {
        console.log(file, "file")
        const formData = new FormData();
        formData.append("file", file);
        formData.append("userId", "12345");
        console.log(formData, "formData")
        const res = await fetch(`${BASE_URL}/extractContent`, {
            method: 'POST',
            body: formData
        });
        let result = await res?.json();
        return result;
    } catch (error) {
        throw error
    }
}

const generateWebsiteFromAi = async (content) => {
    try {
        let prompt= `Analyze the given content getting from resume file and create a single-file solution for a professional and attractive modern static website based on the provided resume content using HTML,CSS and JS only in single file. The design uses a clean layout, modern typography, and a subtle color scheme. I need only code response from your side, no need any other explanation. Content : ${content}`
        let payload = {
            "model": "gpt-oss:120b-cloud",
            "messages": [
                {
                    "role": "user",
                    "content": prompt||""
                }
            ],
            "stream": false
        }
        const res = await fetch(`${BASE_URL}/generateWebsiteFromAi`, {
            method: 'POST',
            body: JSON.stringify(payload),
            headers: {
                "Content-Type": "application/json"
            }
        });
        if (!res.ok) throw new Error("Network response was not ok");
        return res
    } catch (error) {
        throw error
    }
}

export {
    extractContent,
    generateWebsiteFromAi
}