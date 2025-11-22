
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
const publishWebsite = async (siteId) => {
    try {

        const res = await fetch(`${BASE_URL}/publishWebsite/${siteId}`, {
            method: 'POST',
            body: JSON.stringify({})
        });
        let result = await res?.json();
        return result;
    } catch (error) {
        throw error
    }
}

const generateWebsiteFromAi = async (content) => {
    try {
        let payload = {
            "role": "user",
            "content": content||""
        }
        const res = await fetch(`${BASE_URL}/generateWebsiteFromAi`, {
            method: 'POST',
            body: JSON.stringify(payload),
            headers: {
                "Content-Type": "application/json"
            }
        });
         let result = await res?.json();
        return result;
    } catch (error) {
        throw error
    }
}

export {
    extractContent,
    generateWebsiteFromAi,
    publishWebsite
}