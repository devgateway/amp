// @ts-ignore
import React from "react";
import { REST_INDICATOR_REPORT } from "../../utils/constants";

interface FetchIndicatorReportData {
    settings?: Object;
    filters?: Object;
    yearCount?: number;
    setLoading: React.Dispatch<React.SetStateAction<boolean>>
}
export const fetchIndicatorReportData = (indicatorId: number, data: FetchIndicatorReportData) => {
    return new Promise((resolve, reject) => {
        const { settings, filters, yearCount, setLoading } = data;
        setLoading(true);
        const requestBody = {
            ...filters,
            settings: {
                ...settings,
                yearCount: yearCount || 5
            }
        }

        fetch(`${REST_INDICATOR_REPORT}/${indicatorId}`, {
            method: "POST",
            body: JSON.stringify(requestBody),
            headers: {
                "Content-Type": "application/json"
            }
        })
            .then((response) => {
                setLoading(false);
                if (response.ok) {
                    return response.json();
                }
                throw new Error(response.statusText);
            })
            .then((data) => {
                resolve(data);
            })
            .catch((error) => {
                reject(error);
            }).finally(() => {
                setLoading(false);
        })
    });
}
